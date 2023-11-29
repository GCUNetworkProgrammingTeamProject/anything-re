package com.anything.gradproject.controller.api;

import com.anything.gradproject.auth.PrincipalDetail;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import com.anything.gradproject.repository.LecturesRepository;
import com.anything.gradproject.repository.VideoRepository;
import com.anything.gradproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {

    private final MemberService memberService;
    private final PurchaseService purchaseService;
    private final VideoRepository videoRepository;
    private final AnalysisServiceImpl analysisServiceImpl;
    private final ChatbotService chatbotService;
    private final TeacherDetailService teacherDetailService;
    private final FileService fileService;


    @PatchMapping("/update") // 개인정보 수정
    public ResponseEntity<String> updateMember(@RequestBody MemberUpdateDto memberUpdateDto, @RequestHeader("Authorization")String token) throws Exception{
        try {
            memberService.partiallyUpdate(memberUpdateDto, memberService.findMemberByToken(token).getUserSeq());
            return ResponseEntity.status(HttpStatus.OK).body("회원정보가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    //    @GetMapping("/lecture") // 구매한 강의 조회
//
    @PostMapping("/teacher/detail")  // 강사 등록 신청
    public ResponseEntity<String> setTeacherDetail(
            TeacherDetailFormDto teacherDetailFormDto,
            @RequestHeader("Authorization")String token) throws Exception {
        try {

            teacherDetailService.saveTeacherDetail(teacherDetailFormDto, memberService.findMemberByToken(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("강사 등록 신청 완료.");
    }

    @GetMapping("/teachers/detail") // 강사 등록 승인 현황 조회
    public ResponseEntity<Object> printTeacher(@RequestHeader("Authorization")String token) {
        try {
            TeacherDetailResponseDto dto = teacherDetailService.getMyTeacherDetail(memberService.findMemberByToken(token).getUserSeq());
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    // 집중도 분석표 조회 (강의)
    @GetMapping("/analysis/{video_seq}")

    public ResponseEntity<List<AnalysisResponseDto>> getAnalysis(
             @RequestHeader("Authorization")String token,
            @PathVariable long video_seq) {
        
        List<AnalysisResponseDto> dtoList = analysisServiceImpl.getAnalysis(video_seq,memberService.findMemberByToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @GetMapping("/per/analysis") // 집중도 분석표 조회 (개인)
    public ResponseEntity<?> getPerAnalysis(
            @RequestHeader("Authorization") String token) {
        try {
            System.out.println("/per/analysis 요청");
            List<List<AnalysisResponseDto>> dtoLists = analysisServiceImpl.getPerAnalysis(memberService.findMemberByToken(token));
            return ResponseEntity.status(HttpStatus.OK).body(dtoLists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/chatbot/{videoSeq}") // 챗봇 질문내역 조회(강의)
    public ResponseEntity<Object> getLecChatbot(
            @PathVariable long videoSeq,
            @RequestHeader("Authorization")String token) {

        try {
            List<ChatbotResponseDto> dtoList = chatbotService.printChatbot(videoSeq, memberService.findMemberByToken(token));
            return ResponseEntity.status(HttpStatus.OK).body(dtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/per/chatbot") // 챗봇 질문내역 조회(개인)
    public ResponseEntity<?> getperChatbot(
            @RequestHeader("Authorization")String token) {
        try {
            List<List<ChatbotResponseDto>> dtoLists = chatbotService.printPerChatbot(memberService.findMemberByToken(token));
            return ResponseEntity.status(HttpStatus.OK).body(dtoLists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/getVideoSeq") // 결제내역 조회
    public ResponseEntity<List<Long>> getVideoSeq(
            @RequestHeader(value = ("Authorization"), required = false) String token) {

        Member m = memberService.findMemberByToken(token);
        List<Long> videoSeqList = new ArrayList<>();

        if (m != null) {
            List<Lectures> lecturesList = purchaseService.findLecutresByMember(m);
            List<Video> videoList = new ArrayList<>();
            List<Video> tmp;

            for (Lectures l : lecturesList) {
                tmp = videoRepository.findByLectures(l);
                for (Video v : tmp) {
                    videoList.add(v);
                }
            }
            for (Video v : videoList)
                videoSeqList.add(v.getVideoSeq());
            return ResponseEntity.ok(videoSeqList);

        } else
            return null;
    }
    // 결제내역 조회


}
