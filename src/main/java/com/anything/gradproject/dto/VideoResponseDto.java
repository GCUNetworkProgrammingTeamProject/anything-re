package com.anything.gradproject.dto;


import lombok.Data;

@Data

public class VideoResponseDto {

    private long id; // 영상 번호
    private String videoName; // 영상 이름
    private String videoSrc; // filePath
    private int Index; // 영상 순서
    private int duration; // 영상 총 길이(초단위)
    private String videoLectureData; // 강의 자료

}
