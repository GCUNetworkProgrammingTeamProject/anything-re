package com.anything.gradproject.dto;

import com.anything.gradproject.entity.InquiryAnswer;
import lombok.Builder;
import lombok.Data;

@Data
public class AnswerDto {
    private String answer;
    private String teacherName;

    @Builder
    public AnswerDto(InquiryAnswer inquiryAnswer) {
        this.answer = inquiryAnswer.getInquiryAnswer();
        this.teacherName = inquiryAnswer.getMember().getName();
    }
}
