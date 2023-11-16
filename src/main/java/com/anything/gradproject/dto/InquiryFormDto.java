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

    String inquiryTitle;
    String inquiryQuestion;
    boolean inquiryNotice;
    String inquiryIsSecret;
    boolean inquiryIsAnswered;

}