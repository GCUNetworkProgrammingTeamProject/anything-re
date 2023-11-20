package com.anything.gradproject.dto;

import com.anything.gradproject.constant.Role;
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
        if (lectures.getMember().getRole().equals(Role.ADMIN)) {
            this.authorImageSrc = "admin.png";
        } else {
            int teacherDetailIndex = lectures.getMember().getTeacherDetail().size();
            if (teacherDetailIndex == 0) {
                this.authorImageSrc = "noTeacherDetail.png";
            }
            this.authorImageSrc = lectures.getMember().getTeacherDetail().get(teacherDetailIndex - 1).getTeacherImg();
        }
        this.category = lectures.getLecturesType().toString();
    }


}
