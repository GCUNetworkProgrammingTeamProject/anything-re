package com.anything.gradproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class VideoRequestFormDto {


    private long videoSeq;

    private String videoName;

    private MultipartFile videoContent;

    private int videoLength;

    private int videoIndex;

    private MultipartFile videoLectureData;

}