package com.anything.gradproject.dto;

import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.service.CommunityCommentService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor

public class PostResponseDto {
    private long id;
    private String cpTitle;
    private String cpContent;
    private String writer;
    private LocalDateTime modTime;
    private Page<CommentResponseDto> comments;

    public PostResponseDto(CommunityPost communityPost) {
        this.id = communityPost.getCpSeq();
        this.cpContent = communityPost.getCpContent();
        this.cpTitle = communityPost.getCpTitle();
        this.writer = communityPost.getMember().getId();
        this.modTime = communityPost.getModTime();
    }

}
