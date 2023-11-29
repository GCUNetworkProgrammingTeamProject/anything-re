package com.anything.gradproject.service;

import com.anything.gradproject.dto.AnalysisRequestDto;
import com.anything.gradproject.dto.AnalysisResponseDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final VideoAnalysisDetailRepository videoAnalysisDetailRepository;
    private final VideoAnalysisRepository videoAnalysisRepository;
    private final PerVideoAnalysisRepository perVideoAnalysisRepository;
    private final PerVideoAnalysisDetailRepository perVideoAnalysisDetailRepository;
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;

    @Value("${external.api.url}")
    private String url;
    WebClient webClient = WebClient.create("http://localhost:7000");

    public List<AnalysisResponseDto> getAnalysis(long videoSeq, Member member) {
        VideoAnalysis videoAnalysis = videoAnalysisRepository
                .findByMember_UserSeqAndVideo_VideoSeq(member.getUserSeq(), videoSeq).orElseThrow(() -> {
                    return new IllegalArgumentException("해당 강의를 듣고 이용해 주세요.");
                });
        List<AnalysisResponseDto> dtoList = videoAnalysisDetailRepository
                .findByVideoAnalysis_VideoAnalysisSeq(videoAnalysis.getVideoAnalysisSeq())
                .stream()
                .map(AnalysisResponseDto::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<List<AnalysisResponseDto>> getPerAnalysis(Member member) {
        List<PerVideoAnalysis> perVideoAnalysisList = perVideoAnalysisRepository
                .findByMember_UserSeq(member.getUserSeq());
        List<List<AnalysisResponseDto>> dtoLists = new ArrayList<>();
        for (PerVideoAnalysis videoAnalysis : perVideoAnalysisList) {
            List<AnalysisResponseDto> dtoList = perVideoAnalysisDetailRepository
                    .findByPerVideoAnalysis_PerVideoAnalysisSeq(videoAnalysis.getPerVideoAnalysisSeq())
                    .stream().map(AnalysisResponseDto::perEntityToDto).toList();
            dtoLists.add(dtoList);
        }
        return dtoLists;
    }

    @Override
    @Transactional
    public void sendGetRequestAsync(long userSeq, long videoSeq, String recording) {
        Member member = memberRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Video video = videoRepository.findByVideoSeq(videoSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        if (videoAnalysisRepository.findByMember_UserSeqAndVideo_VideoSeq(userSeq, videoSeq).isPresent()) {
            VideoAnalysis deleteVA = videoAnalysisRepository.findByMember_UserSeqAndVideo_VideoSeq(userSeq, videoSeq)
                    .orElseThrow(() -> new IllegalArgumentException("분석표 존재 x"));
            videoAnalysisRepository.delete(deleteVA);
        }
        VideoAnalysis videoAnalysis = videoAnalysisRepository.save(new VideoAnalysis(video, member));


        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/concentrate") // URL의 기본 경로를 포함하여 경로 설정
                        .queryParam("videoName", recording) // 쿼리 파라미터 추가
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<Integer, Float>>() {
                })
                .subscribe(responseData -> {
                    responseData.forEach((key, value) -> {
                        VideoAnalysisDetail vad = new VideoAnalysisDetail(key, value, videoAnalysis);
                        videoAnalysisDetailRepository.save(vad);
                    });
                });
    }

    @Override
    @Transactional
    public void sendPersonalAnalysis(PersonalVideo personalVideo, String recording) {
        try {
            if (perVideoAnalysisRepository.findById(personalVideo.getPersonalVideoSeq()).isEmpty()) {
                PerVideoAnalysis va = new PerVideoAnalysis(personalVideo.getMember(), personalVideo);
                perVideoAnalysisRepository.save(va);
            }
            PerVideoAnalysis perVideoAnalysis = perVideoAnalysisRepository
                    .findByPersonalVideo_PersonalVideoSeq(personalVideo.getPersonalVideoSeq())
                    .orElseThrow(() -> new IllegalArgumentException("해당 분석표가 존재하지 않습니다."));

            webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/concentrate") // URL의 기본 경로를 포함하여 경로 설정
                            .queryParam("videoName", recording) // 쿼리 파라미터 추가
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<Integer, Float>>() {
                    })
                    .subscribe(responseData -> {
                        responseData.forEach((key, value) -> {
                            PerVideoAnalysisDetail vad = new PerVideoAnalysisDetail(key, value, perVideoAnalysis);
                            perVideoAnalysisDetailRepository.save(vad);
                        });
                    });
        } catch (Exception e) {
            throw new RuntimeException("개인영상 분석중 오류");
        }

    }

}
