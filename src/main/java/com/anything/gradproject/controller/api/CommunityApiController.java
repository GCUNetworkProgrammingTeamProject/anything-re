package com.anything.gradproject.controller.api;

import com.anything.gradproject.auth.PrincipalDetail;
import com.anything.gradproject.config.JwtTokenProvider;
import com.anything.gradproject.dto.CommunityPostDto;
import com.anything.gradproject.dto.PostResponseDto;
import com.anything.gradproject.entity.CommunityPost;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.service.CommunityPostService;
import com.anything.gradproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityApiController {


    private final CommunityPostService communityPostService;
    private final MemberService memberService;

//    @PostMapping("/community/new") // 게시글 등록
//    public ResponseEntity<String> Post(@RequestBody CommunityPostDto communityPostDto, @RequestHeader("Authorization")String token) {
//        communityPostService.save(communityPostDto, memberService.findMemberByToken(token));
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body("게시글 등록 완료.");
//    }
    @PostMapping("/community/new") // 게시글 등록
    public ResponseEntity<String> Post(@RequestBody CommunityPostDto communityPostDto, @RequestHeader("Authorization")String token) {
        communityPostService.save(communityPostDto, memberService.findMemberByToken(token));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("게시글 등록 완료.");
    }


    @GetMapping("/community") //전체 게시글 조회
    public ResponseEntity<List<PostResponseDto>> findAllPost() {

        List<PostResponseDto> communityPostPage = communityPostService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(communityPostPage);
    }


    @GetMapping("/community/{id}") // 게시글 상세 조회
    public ResponseEntity<PostResponseDto> postDetail(
            @PathVariable long id,
            @PageableDefault(size = 5, sort = "regTime", direction = Sort.Direction.ASC) Pageable pageable) {

        PostResponseDto postResponseDto = communityPostService.PostDetail(id, pageable);

        return ResponseEntity.ok()
                .body(postResponseDto);
    }


//
    @DeleteMapping("/community/{id}") // 글 번호로 삭제
    public ResponseEntity<String> deletePost(@PathVariable long id, @RequestHeader("Authorization")String token) {
        Member member = memberService.findMemberByToken(token);
        try {
            communityPostService.delete(id, member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


        return ResponseEntity.ok()
                .body("삭제 성공");
    }

    @PatchMapping("/community/{PostId}") //글번호로 업데이트
    public ResponseEntity<String> updatePost(@PathVariable long PostId, @RequestBody CommunityPostDto communityPostDto, @RequestHeader("Authorization")String token) {
        Member member = memberService.findMemberByToken(token);
        communityPostDto.setMember(member);
        try {
            CommunityPost updatedPost = communityPostService.update(PostId, communityPostDto, member);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.ok()
                .body("수정 성공");
    }

}
