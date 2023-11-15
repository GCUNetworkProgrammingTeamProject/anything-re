package com.anything.gradproject.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class VideoFormDto {

    private long lectureSeq;

    private long videoSeq;

    private String videoName;

    private MultipartFile videoContent;

    private int videoLength;

    private int videoIndex;

    private MultipartFile videoLectureData;

}