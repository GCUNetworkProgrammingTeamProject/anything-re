package com.anything.gradproject.service;

import com.anything.gradproject.dto.ChatbotResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.PerChatbotLog;
import com.anything.gradproject.repository.ChatbotLogDetailRepository;
import com.anything.gradproject.repository.ChatbotLogRepository;
import com.anything.gradproject.repository.PerChatbotLogDetailRepository;
import com.anything.gradproject.repository.PerChatbotLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatbotLogRepository chatbotLogRepository;
    private final ChatbotLogDetailRepository chatbotLogDetailRepository;
    private final PerChatbotLogRepository perChatbotLogRepository;
    private final PerChatbotLogDetailRepository perChatbotLogDetailRepository;

    @Override
    public List<ChatbotResponseDto> printChatbot(long videoSeq, Member member) {

        long logSeq = chatbotLogRepository.findByVideo_VideoSeqAndMember_UserSeq(videoSeq, member.getUserSeq())
                .orElseThrow(()-> new IllegalArgumentException("챗봇 질문내역이 없습니다.")).getChatbotLogSeq();
        List<ChatbotResponseDto> dtoList = chatbotLogDetailRepository.findByChatbotLog_ChatbotLogSeq(logSeq)
                .stream()
                .map(ChatbotResponseDto::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }


    @Override
    public List<List<ChatbotResponseDto>> printPerChatbot(Member member) {
        List<Long> logSeqList = perChatbotLogRepository
                .findByMember_UserSeq(member.getUserSeq()).stream().map(PerChatbotLog::getPerChatbotLogSeq).toList();

        List<List<ChatbotResponseDto>> dtoLists = new ArrayList<>();
        for (Long logSeq : logSeqList) {
            dtoLists.add(perChatbotLogDetailRepository.findByPerChatbotLog_PerChatbotLogSeq(logSeq)
                    .stream()
                    .map(ChatbotResponseDto::perEntityToDto)
                    .collect(Collectors.toList()));
        }
        return dtoLists;
    }

}
