package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_teacher_deny")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeacherDeny {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long denySeq;

    @OneToOne
    @JoinColumn(name = "teacher_detail_seq", referencedColumnName = "teacher_detail_seq", unique = true)
    private TeacherDetail teacherDetail;


    @Column
    private String denyReason;

    @Builder
    public TeacherDeny(String denyReason, TeacherDetail teacherDetail) {
        this.denyReason = denyReason;
        this.teacherDetail = teacherDetail;
    }
}