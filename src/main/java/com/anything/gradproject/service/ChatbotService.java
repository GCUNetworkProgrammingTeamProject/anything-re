package com.anything.gradproject.service;

import com.anything.gradproject.dto.ChatbotResponseDto;
import com.anything.gradproject.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ChatbotService {
    public List<ChatbotResponseDto> printChatbot(long videoSeq, Member member);
    public List<List<ChatbotResponseDto>> printPerChatbot(Member member);
}
