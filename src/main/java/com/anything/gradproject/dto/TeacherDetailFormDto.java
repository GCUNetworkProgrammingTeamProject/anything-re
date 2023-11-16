package com.anything.gradproject.dto;

import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.TeacherDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TeacherDetailFormDto {

    private Member member;
    private String teacherIntro; // 강사 소개
    private String teacherCareer; // 강사 경력
    private String teacherField; // 강사 분야
    private MultipartFile file;

    public TeacherDetail toEntity(TeacherDetailFormDto dto, Member member, String saveFileName) {
        return TeacherDetail.builder()
                .member(member)
                .dto(dto)
                .saveFileName(saveFileName)
                .build();
    }

    public void setMember(Member member) {
        this.member = member;
    }
}