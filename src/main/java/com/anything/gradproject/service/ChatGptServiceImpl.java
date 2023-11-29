package com.anything.gradproject.service;

import com.anything.gradproject.dto.PerChatDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ChatGptServiceImpl implements ChatGptService {

    @Value("${openai.api-key}")
    private String apiKey;
    private final ChatbotLogDetailRepository chatbotLogDetailRepository;
    private final ChatbotLogRepository chatbotLogRepository;
    private final VideoRepository videoRepository;
    private final PerChatbotLogRepository perChatbotLogRepository;
    private final PerChatbotLogDetailRepository perChatbotLogDetailRepository;
    private final PersonalVideoRepository personalVideoRepository;

    JSONParser parser = new JSONParser();



    @Override
    public String generateChatResponse(String messages, long videSeq, Member member) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.add("Content-Type", "application/json");
        // ChatGPT API 엔드포인트 URL
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        // 요청 바디 설정
        String requestBody = "{\"model\": \"gpt-3.5-turbo\",\"messages\":[{\"role\": \"user\", \"content\": \"" + messages + "답변은 한국어로 해줘" + "\"}], \"temperature\": 0.7}";
            // 메시지와 원하는 응답 길이 설정
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        // Json 파싱
        String responseBody = response.getBody();
        System.out.println(responseBody);
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            JSONObject choise = (JSONObject) ((JSONArray) jsonObject.get("choices")).get(0);
            JSONObject message = (JSONObject) choise.get("message");
            String content = (String) message.get("content");
            System.out.println(content);
            if (chatbotLogRepository.findByVideo_VideoSeqAndMember_UserSeq(videSeq, member.getUserSeq()).isEmpty()) {
                Video video = videoRepository.findByVideoSeq(videSeq).orElseThrow();
                ChatbotLog chatbotLog1 = new ChatbotLog();
                chatbotLog1.setMember(member);
                chatbotLog1.setVideo(video);
                ChatbotLog saveChatbotLog = chatbotLogRepository.save(chatbotLog1);
                ChatbotLogDetail chatbotLogDetail = new ChatbotLogDetail(messages, content, saveChatbotLog);
                chatbotLogDetailRepository.save(chatbotLogDetail);

            } else {
                ChatbotLog chatbotLog = chatbotLogRepository.findByVideo_VideoSeqAndMember_UserSeq(videSeq, member.getUserSeq()).orElseThrow(()->new IllegalArgumentException("해당 강의에 대한 챗봇로그 정보를 찾을 수 없습니다."));
                ChatbotLogDetail chatbotLogDetail1 = new ChatbotLogDetail(messages, content, chatbotLog);
                chatbotLogDetailRepository.save(chatbotLogDetail1);
            }
            return content;

        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    public String generatePerChatResponse(PerChatDto dto, Member member) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.add("Content-Type", "application/json");
        // ChatGPT API 엔드포인트 URL
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        // 요청 바디 설정
        String requestBody = "{\"model\": \"gpt-3.5-turbo\",\"messages\":[{\"role\": \"user\", \"content\": \"" + dto.getMessages() + "답변은 한국어로 해줘" + "\"}], \"temperature\": 0.7}";
        // 메시지와 원하는 응답 길이 설정
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        // Json 파싱
        String responseBody = response.getBody();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            JSONObject choise = (JSONObject) ((JSONArray) jsonObject.get("choices")).get(0);
            JSONObject message = (JSONObject) choise.get("message");
            String content = (String) message.get("content");
            System.out.println(content);
            if (personalVideoRepository.findByMember_UserSeqAndPersonalVideoCn(member.getUserSeq(), dto.getVideoUrl()).isEmpty()) {
                PersonalVideo video = personalVideoRepository.save(PersonalVideo.builder()
                        .member(member)
                        .personalVideoCn(dto.getVideoUrl())
                        .build());
                PerChatbotLog saveChatbotLog = perChatbotLogRepository.save(PerChatbotLog.builder()
                        .member(member)
                        .personalVideo(video)
                        .build());
                perChatbotLogDetailRepository.save(PerChatbotLogDetail.builder()
                        .perChatbotLog(saveChatbotLog)
                        .answer(content)
                        .question(dto.getMessages())
                        .build());

            }else if (personalVideoRepository.findByMember_UserSeqAndPersonalVideoCn(member.getUserSeq(), dto.getVideoUrl()).isPresent() &&
                    perChatbotLogRepository.findByPersonalVideo_PersonalVideoCnAndMember_UserSeq(dto.getVideoUrl(), member.getUserSeq()).isEmpty()) {
                PersonalVideo video = personalVideoRepository.findByMember_UserSeqAndPersonalVideoCn(member.getUserSeq(), dto.getVideoUrl()).get();
                PerChatbotLog saveChatbotLog = perChatbotLogRepository.save(PerChatbotLog.builder()
                        .member(member)
                        .personalVideo(video)
                        .build());
                perChatbotLogDetailRepository.save(PerChatbotLogDetail.builder()
                        .perChatbotLog(saveChatbotLog)
                        .answer(content)
                        .question(dto.getMessages())
                        .build());

            } else {
                PerChatbotLog chatbotLog = perChatbotLogRepository
                        .findByPersonalVideo_PersonalVideoCnAndMember_UserSeq(dto.getVideoUrl(), member.getUserSeq())
                        .orElseThrow(() -> new IllegalArgumentException("해당 강의에 대한 챗봇로그 정보를 찾을 수 없습니다."));
                perChatbotLogDetailRepository.save(PerChatbotLogDetail.builder()
                        .perChatbotLog(chatbotLog)
                        .answer(content)
                        .question(dto.getMessages())
                        .build());
            }

            return content;

        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
