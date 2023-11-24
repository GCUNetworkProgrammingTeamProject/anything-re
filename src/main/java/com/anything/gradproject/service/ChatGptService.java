package com.anything.gradproject.service;

import com.anything.gradproject.dto.PerChatDto;
import com.anything.gradproject.entity.Member;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ChatGptService {
    public String generateChatResponse(String messages, long videoSeq, Member member);
    public String generatePerChatResponse(PerChatDto dto, Member member);


}
