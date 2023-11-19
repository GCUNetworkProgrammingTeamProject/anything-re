package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
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