package com.anything.gradproject.dto;

import com.anything.gradproject.constant.LecturesType;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class LectureUpdateDto {
    private String lectureName; // 강의 이름
    private String lectureContent; // 강의 설명
    private Integer lectureIndex; // 영상 갯수
    private Integer lecturePrice; // 강의 가격
    private String lecturesType; //강의 타입
}
