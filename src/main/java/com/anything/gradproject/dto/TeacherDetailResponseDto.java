package com.anything.gradproject.dto;

import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.TeacherDetail;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
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

    public static TeacherDetailResponseDto toDto(TeacherDetail teacherDetail) {
        TeacherDetailResponseDto dto = new TeacherDetailResponseDto();
        dto.setTeacherName(teacherDetail.getMember().getName());
        dto.setUserSeq(teacherDetail.getMember().getUserSeq());
        dto.setTeacherDetailSeq(teacherDetail.getTeacherDetailSeq());
        dto.setTeacherField(teacherDetail.getTeacherField());
        dto.setTeacherCareer(teacherDetail.getTeacherCareer());
        dto.setTeacherImg(teacherDetail.getTeacherImg());
        dto.setTeacherIntro(teacherDetail.getTeacherIntro());
        dto.setTeacherStatus(teacherDetail.getMember().getTeacherStatus());
        if (teacherDetail.getMember().getTeacherStatus() == TeacherStatus.REFUSE) {
            dto.setReason("거절 사유 : " + teacherDetail.getTeacherDeny().getDenyReason());
        } else if (teacherDetail.getMember().getTeacherStatus() == TeacherStatus.APPROVE) {
            dto.setReason("강사 신청이 승인 되었습니다.");
        } else {
            dto.setReason("승인 대기중 입니다.");
        }

        return dto;
    }
}
