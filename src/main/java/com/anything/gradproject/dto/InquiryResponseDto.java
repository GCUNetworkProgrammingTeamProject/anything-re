package com.anything.gradproject.dto;

import com.anything.gradproject.entity.Inquiry;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InquiryResponseDto {

    private long inquirySeq; // 문의 번호
    private String inquiryTitle; // 질문 제목
    private String inquiryQuestion; // 질문 내용
    private boolean inquiryNotice; // 공지사항유무 ture = 공지사항, false = 일반
    private boolean inquiryIsAnswered; // 답변 유무
    private String questioner; // 질문자
    private String date; // 문의 일자
    private List<AnswerDto> answerList;
    private List<String> respondentList; // 답변자
    private List<String> inquiryAnswerList; // 질문에 대한 답변

    @Builder
    public InquiryResponseDto(Inquiry inquiry) {
        this.inquirySeq = inquiry.getInquirySeq();
        this.inquiryIsAnswered = inquiry.isInquiryIsAnswered();
        this.inquiryTitle = inquiry.getInquiryTitle();
        this.inquiryQuestion = inquiry.getInquiryQuestion();
        this.inquiryNotice = inquiry.isInquiryNotice();
        this.questioner = inquiry.getMember().getName();
        this.date = inquiry.getRegTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        this.answerList = inquiry.getInquiryAnswerList()
                .stream()
                .map(
                        (inquiryAnswer) -> AnswerDto
                                .builder()
                                .inquiryAnswer(inquiryAnswer)
                                .build())
                .collect(Collectors.toList());
    }


    }


