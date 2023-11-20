package com.anything.gradproject.dto;

import com.anything.gradproject.constant.Role;
import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberResponseDto {

    private Long userSeq; // 회원번호
    private Role role; // 회원 등급
    private String name; // 회원 이름
    private String id; // 회원 아이디
    private String email; // 회원 이메일
    private TeacherStatus teacherStatus; //강사 승인 여부, 일반 유저 초기값 USER , 강사 회원가입시 초기값 WAIT,

    @Builder
    public MemberResponseDto(Member member) {
        this.userSeq = member.getUserSeq();
        this.role = member.getRole();
        this.name = member.getName();
        this.id = member.getId();
        this.email = member.getEmail();
        this.teacherStatus = member.getTeacherStatus();
    }


}
