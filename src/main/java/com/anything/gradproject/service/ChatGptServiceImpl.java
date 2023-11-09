package com.anything.gradproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ChatGptServiceImpl implements ChatGptService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient webClient;
    @Override
    public Mono<String> generateChatResponse(String message, long videSeq) {
        // ChatGPT API 엔드포인트 URL
        String apiUrl = "https://api.openai.com/v1/engines/davinci-codex/completions";

        // WebClient를 사용하여 API 호출
        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 요청 바디 설정
        String requestBody = "{\"prompt\":\"" + message + "\",\"max_tokens\":50}"; // 메시지와 원하는 응답 길이 설정

        // API 호출 및 응답 처리
        return webClient.post()
                .uri("")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class);
    }
}
