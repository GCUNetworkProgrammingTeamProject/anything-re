package com.anything.gradproject.dto;

import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommunityPostDto {

    private String cpTitle;
    private String cpContent;
    private String cpType;
    private Member member;

    public CommunityPost toEntity() {
        return CommunityPost.builder()
                .title(cpTitle)
                .content(cpContent)
                .member(member)
                .build();
    }

    public void setMember(Member member) {
        this.member = member;
    }


}
