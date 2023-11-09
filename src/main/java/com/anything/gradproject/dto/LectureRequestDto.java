package com.anything.gradproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class LectureRequestDto {

    private long lectureSeq;

    private String lectureName;

    private String lectureContent;

    private int lectureIndex;

    private int lecturePrice;

}
