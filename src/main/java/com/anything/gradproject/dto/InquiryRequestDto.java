package com.anything.gradproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquiryRequestDto {

    long userSeq;

    long videoSeq;

    long lectureSeq;

    long inquirySeq;

    String inquiryTitle;

    String inquiryQuestion;

    String inquiryName;

    boolean inquiryNotice;

    String inquiryIsSecret;

}