package com.anything.gradproject.dto;

import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.entity.FileEntity;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class LecturesFormDto {


    @NotBlank(message = "강의명을 입력해 주세요")
    private String lectureName;

    @NotBlank(message = "강의 설명을 입력해 주세요")
    private String lectureContent;

    @NotEmpty(message = "강의 순서를 입력해 주세요")
    private int lectureIndex;

    @NotEmpty(message = "강의 가격을 입력해 주세요")
    private int lecturePrice;

    @NotEmpty(message = "강의 섬네일을 입력해 주세요")
    private MultipartFile lectureImage;

    @NotEmpty(message = "강의 카테고리를 입력해 주세요")
    private String lecturesType;

    private MultipartFile Video;

    private MultipartFile Data;
    private Integer videoDuration; // 영상 재생시간

}