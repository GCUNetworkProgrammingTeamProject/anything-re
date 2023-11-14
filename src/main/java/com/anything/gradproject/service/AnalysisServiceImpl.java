package com.anything.gradproject.service;

import com.anything.gradproject.dto.AnalysisRequestDto;
import com.anything.gradproject.dto.AnalysisResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import com.anything.gradproject.entity.VideoAnalysis;
import com.anything.gradproject.entity.VideoAnalysisDetail;
import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.repository.VideoAnalysisDetailRepository;
import com.anything.gradproject.repository.VideoAnalysisRepository;
import com.anything.gradproject.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final VideoAnalysisDetailRepository videoAnalysisDetailRepository;
    private final VideoAnalysisRepository videoAnalysisRepository;
    private final WebClient webClient;
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;

    @Value("${external.api.url}")
    private String url;


    public List<AnalysisResponseDto> getAnalysis(long videoSeq, Member member) {
        VideoAnalysis videoAnalysis = videoAnalysisRepository.findByMember_UserSeqAndVideo_VideoSeq(member.getUserSeq(), videoSeq).orElseThrow(() -> {
            return new IllegalArgumentException("해당 강의를 듣고 이용해 주세요.");
        });
        List<AnalysisResponseDto> dtoList = videoAnalysisDetailRepository.findByVideoAnalysis_VideoAnalysisSeq(videoAnalysis.getVideoAnalysisSeq())
                .stream()
                .map(AnalysisResponseDto::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public String sendGetRequest(long userSeq, long videoSeq, String recording) {
        String result;
        Member member = memberRepository.findByUserSeq(userSeq).orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        Video video = videoRepository.findByVideoSeq(videoSeq).orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        try {
            if (videoAnalysisRepository.findByMember_UserSeqAndVideo_VideoSeq(userSeq, videoSeq).isEmpty()) {
                VideoAnalysis va = new VideoAnalysis(video, member);
                videoAnalysisRepository.save(va);
            }
            VideoAnalysis videoAnalysis = videoAnalysisRepository.findByMember_UserSeqAndVideo_VideoSeq(userSeq, videoSeq).orElseThrow(()->new IllegalArgumentException("해당 분석표가 존재하지 않습니다."));

            AnalysisRequestDto dto = new AnalysisRequestDto();
            dto.setUserSeq(userSeq);
            dto.setRecording(recording);
            dto.setVideoSeq(videoSeq);
            Mono<Map<Integer, Float>> responseDataMono = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path(url).queryParam("url", recording).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<Integer, Float>>() {
                    });

            responseDataMono.subscribe(responseData -> {
                for (Map.Entry<Integer, Float> entry : responseData.entrySet()) {
                    VideoAnalysisDetail vad = new VideoAnalysisDetail(entry.getKey(), entry.getValue(), videoAnalysis);
                    videoAnalysisDetailRepository.save(vad);
                }
            });
            result = "집중도 저장이 완료되었습니다.";
        } catch (Exception e) {
            result = e.getMessage();
        }


        return result;
    }

}
