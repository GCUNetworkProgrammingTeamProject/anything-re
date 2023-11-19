package com.anything.gradproject.entity;

import com.anything.gradproject.dto.TeacherDetailFormDto;
import com.anything.gradproject.dto.TeacherDetailResponseDto;
import com.anything.gradproject.dto.VideoFormDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_teacher_detail")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeacherDetail  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_detail_seq")
    private long teacherDetailSeq;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member;

    @Column
    private String teacherIntro; // 강사 소개

    @Column
    private String teacherCareer; // 강사 경력

    @Column
    private String teacherImg; // 강사 이미지

    @Column
    private String teacherField; // 강사 분야

    @OneToOne(mappedBy = "teacherDetail", fetch = FetchType.LAZY)
    private TeacherDeny teacherDeny;





    @Builder
    public TeacherDetail(TeacherDetailFormDto dto, Member member, String saveFileName) {
        this.member = member;
        this.teacherCareer = dto.getTeacherCareer();
        this.teacherField = dto.getTeacherField();
        this.teacherIntro = dto.getTeacherIntro();
        this.teacherImg = saveFileName;
    }



}