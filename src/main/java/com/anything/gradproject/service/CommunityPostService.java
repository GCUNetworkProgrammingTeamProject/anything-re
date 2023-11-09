package com.anything.gradproject.service;

import com.anything.gradproject.constant.Role;
import com.anything.gradproject.dto.CommunityPostDto;
import com.anything.gradproject.dto.PostResponseDto;
import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.UploadedFile;
import com.anything.gradproject.repository.CommunityPostRepository;
import com.anything.gradproject.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityPostService {

    @Value("${file.dir}")
    private String fileDir;

    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentService communityCommentService;

    @Transactional
    public CommunityPost save(CommunityPostDto communityPostDto, Member member) {
        communityPostDto.setMember(member);


        return communityPostRepository.save(communityPostDto.toEntity());
    }

    public List<PostResponseDto> findAll() {
        List<CommunityPost> communityPostList = communityPostRepository.findAll();
        List<PostResponseDto> dtos = new ArrayList<>();

        for (CommunityPost communityPost : communityPostList) {
            dtos.add(convertToDto(communityPost));
        }
        return dtos;
    }

    public PostResponseDto convertToDto(CommunityPost communityPost) {
        PostResponseDto dto = new PostResponseDto(communityPost);
        return dto;
    }


    public CommunityPost findById(long id) { //글번호로 찾기
        return communityPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public PostResponseDto PostDetail(long id, Pageable pageable) {
        CommunityPost communityPost = this.findById(id);
        PostResponseDto dto = new PostResponseDto();
        dto.setId(communityPost.getCpSeq());
        dto.setCpTitle(communityPost.getCpTitle());
        dto.setCpContent(communityPost.getCpContent());
        dto.setWriter(communityPost.getMember().getId());
        dto.setModTime(communityPost.getModTime());
        dto.setComments(communityCommentService.findByCpSeq(id, pageable));
        return dto;
    }

    @Transactional
    public void delete(long PostId, Member member) {
        CommunityPost communityPost = communityPostRepository.findById(PostId)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + PostId));
        if (communityPost.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
            communityPostRepository.deleteById(PostId);
        } else {
            throw new IllegalStateException("권한이 없습니다.");
        }

    }

    @Transactional
    public CommunityPost update(long PostId, CommunityPostDto communityPostDto, Member member) {
        CommunityPost communityPost = communityPostRepository.findById(PostId)
                .orElseThrow(() -> new IllegalArgumentException("not found:" + PostId));
        if (communityPost.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
            communityPost.update(communityPostDto.getCpTitle(), communityPostDto.getCpContent(), member);
        } else {
            throw new IllegalStateException("권한이 없습니다.");
        }


        return communityPost;
    }

}
