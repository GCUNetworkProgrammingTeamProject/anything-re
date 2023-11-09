package com.anything.gradproject.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InquiryFormDto {

    long userSeq;

    long videoSeq;

    long lectureSeq;

    long inquirySeq;

    String inquiryTitle;

    String inquiryQuestion;

    String inquiryName;

    boolean inquiryNotice;

    String inquiryIsSecret;

    boolean inquiryIsAnswered;

}