package com.anything.gradproject.dto;

import com.anything.gradproject.entity.Lectures;
import lombok.Builder;
import lombok.Data;

@Data
public class LectureResponseDto {

    private String title;
    private long id;
    private String desc; // 설명
    private int lessonCount;
    private int originalPrice;
    private String imageSrc;
    private boolean paid;
    private String authorName;
    private String authorImageSrc;
    private String category;

    @Builder
    public LectureResponseDto(Lectures lectures) {
        this.title = lectures.getLectureName();
        this.id = lectures.getLectureSeq();
        this.desc = lectures.getLectureContent();
        this.lessonCount = lectures.getLectureIndex();
        this.originalPrice = lectures.getLecturePrice();
        this.imageSrc = lectures.getLectureImage();
        this.paid = true;
        this.authorName = lectures.getMember().getName();
        this.authorImageSrc = lectures.getMember().getTeacherDetail().get(lectures.getMember().getTeacherDetail().size() - 1).getTeacherImg();
        this.category = lectures.getLecturesType().toString();
    }


}
