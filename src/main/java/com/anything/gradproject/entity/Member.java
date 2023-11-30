package com.anything.gradproject.entity;


import com.anything.gradproject.constant.Role;
import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.dto.MemberFormDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Entity
@Table(name = "tb_member")
@Getter @Setter
@ToString
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq; // 회원번호

    @Enumerated(EnumType.STRING)
    private Role role; // 회원 등급

    private String name; // 회원 이름

    @Column(unique = true)
    private String id; // 회원 아이디

    private String password; // 회원 패스워드

    @Column(unique = true)
    private String email; // 회원 이메일

    @Enumerated(EnumType.STRING)
    private TeacherStatus teacherStatus; //강사 승인 여부, 일반 유저 초기값 USER , 강사 회원가입시 초기값 WAIT,


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "member"})
    private List<TeacherDetail> teacherDetail;


    @Builder
    public Member(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        this.id = memberFormDto.getId();
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.password = passwordEncoder.encode(memberFormDto.getPassword());

        if ("0".equals((memberFormDto.getRole()))) {
            this.role = Role.USER;
            this.teacherStatus = TeacherStatus.USER;
        } else if ("1".equals((memberFormDto.getRole()))) {
            this.role = Role.LECTURER;
            this.teacherStatus = TeacherStatus.WAIT;
        }
    }




}
