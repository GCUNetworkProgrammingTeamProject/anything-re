package com.anything.gradproject.dto;

import lombok.Data;

@Data
public class LectureResponseDto {

    private String title;
    private long id;
    private String desc;
    private int lessonCount;
    private int originalPrice;
    private String imageSrc;
    private boolean paid;
    private String authorName;

}
