package com.anything.gradproject.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ChatGptService {
    public Mono<String> generateChatResponse(String message, long videoSeq);
}
