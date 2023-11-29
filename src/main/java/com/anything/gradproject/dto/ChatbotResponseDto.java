package com.anything.gradproject.dto;

import com.anything.gradproject.entity.ChatbotLogDetail;
import com.anything.gradproject.entity.PerChatbotLogDetail;
import com.anything.gradproject.entity.VideoAnalysisDetail;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatbotResponseDto {
    private String question;
    private String answer;
    private long perVideoSeq;


    @Builder
    public ChatbotResponseDto(ChatbotLogDetail chatbotLogDetail) {
        this.question = chatbotLogDetail.getQuestion();
        this.answer = chatbotLogDetail.getAnswer();
    }

    public static ChatbotResponseDto entityToDto(ChatbotLogDetail chatbotLogDetail) {
        return ChatbotResponseDto
                .builder()
                .chatbotLogDetail(chatbotLogDetail)
                .build();
    }
    public static ChatbotResponseDto perEntityToDto(PerChatbotLogDetail perChatbotLogDetail) {
        ChatbotResponseDto dto = new ChatbotResponseDto();
        dto.setAnswer(perChatbotLogDetail.getAnswer());
        dto.setQuestion(perChatbotLogDetail.getQuestion());
        dto.setPerVideoSeq(perChatbotLogDetail.getPerChatbotLog().getPerChatbotLogSeq());
        return dto;
    }
}
