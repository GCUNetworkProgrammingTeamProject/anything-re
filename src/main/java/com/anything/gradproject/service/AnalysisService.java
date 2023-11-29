package com.anything.gradproject.service;

import com.anything.gradproject.dto.AnalysisRequestDto;
import com.anything.gradproject.dto.AnalysisResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.PersonalVideo;
import com.anything.gradproject.entity.VideoAnalysis;
import com.anything.gradproject.repository.VideoAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface AnalysisService {
    List<AnalysisResponseDto> getAnalysis(long videoSeq, Member member);
    List<List<AnalysisResponseDto>> getPerAnalysis(Member member);

//    public String sendGetRequest(long userSeq, long videoSeq, String recording);

    void sendGetRequestAsync(long userSeq, long videoSeq, String recording);

    void sendPersonalAnalysis(PersonalVideo PerVideo, String recording);
}
