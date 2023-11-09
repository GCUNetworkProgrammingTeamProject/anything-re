package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_progress_table")
@Getter @Setter
@ToString
@NoArgsConstructor
public class ProgressTable {
    @Id
    private long progressTableSeq;

    @Column
    private String progress; // 강의 진도

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Member member;

    @OneToOne
    @JoinColumn(name = "video_seq")
    private Video video;
}
