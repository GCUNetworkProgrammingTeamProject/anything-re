package com.anything.gradproject.controller.api;

import com.anything.gradproject.auth.PrincipalDetail;
import com.anything.gradproject.dto.CommentResponseDto;
import com.anything.gradproject.dto.CommentUpdateDto;
import com.anything.gradproject.dto.CommunityCommentDto;
import com.anything.gradproject.service.CommunityCommentService;
import com.anything.gradproject.service.CommunityPostService;
import com.anything.gradproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CcApiController {

    // 커뮤니티 댓글 조회/생성/삭제
    // Controller
    // - 요청경로 매핑
    // 댓글 목록 - 화면		GET 	/community/{postId}/comments        -> 프론트 미구현

    // 댓글 쓰기 - 화면		GET 	/community/{postId}                        -> 프론트 미구현
    // 댓글 쓰기 - 화면		POST 	/community/{postId}/new

    // 댓글 삭제 - 처리		DELETE	/community/comments/{coId}


    private final CommunityCommentService communityCommentService;

    private final CommunityPostService communityPostService;
    private final MemberService memberService;

    @PostMapping("/community/{postId}/new")
    public ResponseEntity<String> createComment(@PathVariable long postId, @RequestBody CommunityCommentDto CommunityCommentDto, @RequestHeader("Authorization") String token) {
        communityCommentService.save(CommunityCommentDto, memberService.findMemberByToken(token), communityPostService.findById(postId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("댓글 작성 완료");
    }

    @GetMapping("/community/{postId}/comments") // 댓글 불러오기
    public ResponseEntity<Page<CommentResponseDto>> printComments(
            @PathVariable long postId,
            @PageableDefault(size = 5, sort = "regTime", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CommentResponseDto> pages = communityCommentService.findByCpSeq(postId, pageable);

        return new ResponseEntity<>(pages, HttpStatus.OK);
    }


    @DeleteMapping("/community/comments/{coId}") // 댓글 번호로 삭제
    public ResponseEntity<String> deleteComment(@PathVariable long coId, @RequestHeader("Authorization") String token) {

        communityCommentService.deleteComment(coId, memberService.findMemberByToken(token));

        return ResponseEntity.status(HttpStatus.OK)
                .body("삭제가 완료되었습니다.");
    }

    @PatchMapping("/community/comments/{coId}")
    public ResponseEntity<String> updateComment(@PathVariable long coId, @RequestBody CommentUpdateDto dto,  @RequestHeader("Authorization") String token) {
        try {
            communityCommentService.updateComment(coId, dto, memberService.findMemberByToken(token));
            return ResponseEntity.status(HttpStatus.OK).body("댓글 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
