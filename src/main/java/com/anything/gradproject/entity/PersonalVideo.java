package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_personal_video")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PersonalVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personalVideoSeq; // 개인 영상 번호

    @Column
    private String personalVideoCn; // 개인 영상 링크

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Member member;

    @Builder
    public PersonalVideo(String personalVideoCn, Member member) {
        this.personalVideoCn = personalVideoCn;
        this.member = member;
    }


}
