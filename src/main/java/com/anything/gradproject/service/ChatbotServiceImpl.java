package com.anything.gradproject.service;

import com.anything.gradproject.dto.ChatbotResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.repository.ChatbotLogDetailRepository;
import com.anything.gradproject.repository.ChatbotLogRepository;
import com.anything.gradproject.repository.PerChatbotLogDetailRepository;
import com.anything.gradproject.repository.PerChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatbotLogRepository chatbotLogRepository;
    private final ChatbotLogDetailRepository chatbotLogDetailRepository;
    private final PerChatbotLogRepository logRepository;
    private final PerChatbotLogDetailRepository logDetailRepository;

    @Override
    public List<ChatbotResponseDto> printChatbot(long videoSeq, Member member) {
        long logSeq = chatbotLogRepository.findByVideo_VideoSeqAndMember_UserSeq(videoSeq, member.getUserSeq()).orElseThrow(()-> new IllegalArgumentException("해당 영상을 찾을 수 없습니다.")).getChatbotLogSeq();
        // System.out.println("logSeq: "+logSeq);
        List<ChatbotResponseDto> dtoList = chatbotLogDetailRepository.findByChatbotLog_ChatbotLogSeq(logSeq)
                .stream()
                .map(ChatbotResponseDto::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }


    @Override
    public List<ChatbotResponseDto> printPerChatbot(long videoSeq, Member member) {
        long logSeq = logRepository.findByPersonalVideo_PersonalVideoSeqAndMember_UserSeq(videoSeq, member.getUserSeq());
        List<ChatbotResponseDto> dtoList = logDetailRepository.findByPerChatbotLog_PerChatbotLogSeq(logSeq)
                .stream()
                .map(ChatbotResponseDto::perEntityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

}
