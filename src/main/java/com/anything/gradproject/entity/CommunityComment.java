package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tb_community_comment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ccSeq;

    private String ccContent;

    @ManyToOne
    @JoinColumn(name = "cp_seq")
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 post가 삭제되면 같이 삭제됨
    private CommunityPost communityPost;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    private Member member;

    @Builder
    public CommunityComment(String content, Member member, CommunityPost communityPost) {
        this.ccContent = content;
        this.member = member;
        this.communityPost = communityPost;
    }
}
