package com.anything.gradproject.dto;

import com.anything.gradproject.entity.CommunityComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private long id;
    private String ccContent;
    private String writer;
    private LocalDateTime modTime;

    public CommentResponseDto (CommunityComment communityComment) {
        this.id = communityComment.getCcSeq();
        this.ccContent = communityComment.getCcContent();
        this.writer = communityComment.getMember().getId();
        this.modTime = communityComment.getModTime();
    }

}
