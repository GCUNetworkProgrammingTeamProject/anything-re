package com.anything.gradproject.service;

import com.anything.gradproject.constant.Role;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.CommunityComment;
import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.repository.CommunityCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {

    private final CommunityCommentRepository communityCommentRepository;
    @Transactional
    public CommunityComment save(CommunityCommentDto communityCommentDto, Member member, CommunityPost communityPost) {
        communityCommentDto.setMember(member);
        communityCommentDto.setCommunityPost(communityPost);
        return communityCommentRepository.save(communityCommentDto.toEntity());
    }

    public Page<CommentResponseDto> findByCpSeq(long cpSeq, Pageable pageable) {
       Page<CommunityComment> comments = communityCommentRepository.findByCommunityPost_CpSeq(cpSeq, pageable);

       return  comments.map(this::convertToDto);
    }


    public CommentResponseDto convertToDto(CommunityComment communityComment) {
        CommentResponseDto dto = new CommentResponseDto(communityComment);
        return dto;
    }

    public void deleteComment(long id, Member member) {

        CommunityComment target = communityCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (target.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
            communityCommentRepository.delete(target);
        } else {
            throw new IllegalStateException("권한이 없습니다.");
        }

    }
    @Transactional
    public void updateComment(long coId, CommentUpdateDto dto, Member member) {
        CommunityComment comment = communityCommentRepository.findById(coId).orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        if (comment.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
            try {
                comment.setCcContent(dto.getContent());
            } catch (Exception e) {
                throw new RuntimeException("댓글 업데이트 중 오류가 발생했습니다.");
            }
        } else {
            throw new IllegalStateException("권한이 없습니다.");
        }

    }

}
