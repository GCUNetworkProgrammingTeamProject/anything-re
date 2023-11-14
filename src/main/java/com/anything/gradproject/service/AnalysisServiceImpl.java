package com.anything.gradproject.service;

import com.anything.gradproject.dto.AnalysisRequestDto;
import com.anything.gradproject.dto.AnalysisResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.VideoAnalysis;
import com.anything.gradproject.repository.VideoAnalysisDetailRepository;
import com.anything.gradproject.repository.VideoAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final VideoAnalysisDetailRepository videoAnalysisDetailRepository;
    private final VideoAnalysisRepository videoAnalysisRepository;
    private final WebClient webClient;

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
    public Mono<ResponseEntity<String>> sendPostRequest(long userSeq, long videoSeq, String recording) {

        AnalysisRequestDto dto = new AnalysisRequestDto();
        dto.setUserSeq(userSeq);
        dto.setRecording(recording);
        dto.setVideoSeq(videoSeq);
        String url = "/concentrate";
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .toEntity(String.class);
    }

}
