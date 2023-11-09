package com.anything.gradproject.dto;

import com.anything.gradproject.entity.CommunityComment;
import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CommunityCommentDto {
    private String ccContent;
    private Member member;
    private CommunityPost communityPost;


    public CommunityComment toEntity() {
        return CommunityComment.builder()
                .content(ccContent)
                .communityPost(communityPost)
                .member(member)
                .build();
    }




    public void setMember(Member member) {
        this.member = member;
    }
    public void setCommunityPost(CommunityPost communityPost) {
        this.communityPost = communityPost;
    }
}
