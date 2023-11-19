package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member;

    @Builder
    public PersonalVideo(String personalVideoCn, Member member) {
        this.personalVideoCn = personalVideoCn;
        this.member = member;
    }


}
