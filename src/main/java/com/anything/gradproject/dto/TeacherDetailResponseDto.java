package com.anything.gradproject.dto;

import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.TeacherDetail;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
public class TeacherDetailResponseDto {
    private long teacherDetailSeq; // 신청번호
    private String teacherIntro; // 강사 소개
    private String teacherCareer; // 강사 경력
    private String TeacherImg; // 강사 사진
    private String teacherField; // 강사 분야
    private String TeacherName; //강사 이름
    private String reason;
    private TeacherStatus teacherStatus;
    private long userSeq; // 회원번호
}
