package com.anything.gradproject.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AdvertiseFormDto {


    private long adverSeq;

    @NotBlank(message = "광고명을 입력해 주세요")
    private String adverName;


    private long adverClicker;

    @NotEmpty(message = "광고 url을 입력해 주세요")
    private String adverUrl;

    @NotEmpty(message = "광고 이미지를 입력해 주세요")
    private MultipartFile adverImage;


    private boolean isBanner;

}