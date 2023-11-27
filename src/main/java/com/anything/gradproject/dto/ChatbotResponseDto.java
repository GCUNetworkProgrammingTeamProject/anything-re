package com.anything.gradproject.dto;

import com.anything.gradproject.entity.ChatbotLogDetail;
import com.anything.gradproject.entity.PerChatbotLogDetail;
import com.anything.gradproject.entity.VideoAnalysisDetail;
import lombok.Builder;
import lombok.Data;

@Data
public class ChatbotResponseDto {
    private String question;
    private String answer;
    private long perVideoSeq;


    @Builder
    public ChatbotResponseDto(ChatbotLogDetail chatbotLogDetail) {
        this.question = chatbotLogDetail.getQuestion();
        this.answer = chatbotLogDetail.getAnswer();
    }
    @Builder(builderMethodName = "personal")
    public ChatbotResponseDto(PerChatbotLogDetail perChatbotLogDetail) {
        this.question = perChatbotLogDetail.getQuestion();
        this.answer = perChatbotLogDetail.getAnswer();
        this.perVideoSeq = perChatbotLogDetail.getPerChatbotLog().getPerChatbotLogSeq();
    }

    public static ChatbotResponseDto entityToDto(ChatbotLogDetail chatbotLogDetail) {
        return ChatbotResponseDto
                .builder()
                .chatbotLogDetail(chatbotLogDetail)
                .build();
    }
    public static ChatbotResponseDto perEntityToDto(PerChatbotLogDetail perChatbotLogDetail) {
        return ChatbotResponseDto
                .personal()
                .perChatbotLogDetail(perChatbotLogDetail)
                .build();
    }
}
