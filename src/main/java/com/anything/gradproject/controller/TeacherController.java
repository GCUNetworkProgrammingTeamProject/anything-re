package com.anything.gradproject.controller;

import com.anything.gradproject.auth.PrincipalDetail;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import com.anything.gradproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

import static org.thymeleaf.util.StringUtils.substring;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/t")
public class TeacherController {

    private final LecturesRepository lecturesRepository;
    private final VideoRepository videoRepository;
    private final InquiryRepository inquiryRepository;
    private final LectureService lectureService;
    private final VideoService videoService;
    private final InquiryService inquiryService;
    private final FileService fileService;
    private final InquiryAnswerRepository inquiryAnswerRepository;
    private final MemberService memberService;


    // 강의 안에 동영상 출력
    @GetMapping("/lectures/v/{lectureSeq}")
    public ResponseEntity<List<VideoResponseDto>> printVideov(@PathVariable long lectureSeq) {

        List<VideoResponseDto> dtoList = videoService.findVideo(lectureSeq);

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }


    // 강의 목록 출력
    @GetMapping("/lectures")
    public ResponseEntity<List<Lectures>> printLectures(@RequestHeader("Authorization")String token) {

        List<Lectures> lecturesList = lectureService.findUserLectureList(memberService.findMemberByToken(token));
        return ResponseEntity.ok(lecturesList);
    }



    // 강의 등록
    @PostMapping("/lectures")
    public ResponseEntity<String> createLectures( LecturesFormDto lecturesFormDto,
            @RequestHeader("Authorization")String token) throws IOException {
        try {
            Lectures lectures = Lectures.createLectures(lecturesFormDto, memberService.findMemberByToken(token));
            String fileName = fileService.saveFile(lecturesFormDto.getLectureImage(), lectures.getLectureImage());
	    lectures.setLecturesType(lectureService.setLecturesType(lecturesFormDto.getLecturesType()));
            lectures.setLectureImage(fileName);
            lecturesRepository.save(lectures);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("강의 등록 완료");
    }


    // 강의 수정
    @PutMapping("/lectures/{lectureSeq}")
    public ResponseEntity<String> updateLectures(@PathVariable long lectureSeq, LecturesFormDto lecturesFormDto) throws IOException {

        try {
            Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
            lectures.setLectureImage(lectures.getLectureImage().substring(0, lectures.getLectureImage().lastIndexOf(".")));
            Lectures.modifyLectures(lecturesFormDto, lectures);
            String fileName = fileService.saveFile(lecturesFormDto.getLectureImage(), lectures.getLectureImage());
            lectures.setLectureImage(fileName);
            lecturesRepository.save(lectures);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("강의 변경 완료");
    }


    // 강의 삭제
    @DeleteMapping("/lectures/{lectureSeq}")
    public ResponseEntity<String> deleteLectures(@PathVariable long lectureSeq) {

        try {
            Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
            fileService.removeFile(lectures.getLectureImage());
            lecturesRepository.delete(lectures);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("강의 삭제 완료");

    }






    // 강의의 영상 목록 출력
    @GetMapping("/lectures/{lectureSeq}")
    public ResponseEntity<List<Video>> printVideo(@PathVariable long lectureSeq) {
        List<Video> videoList = videoService.findLectureVideoList(lectureSeq);

        return ResponseEntity.ok(videoList);
    }

    // 영상 등록
    @PostMapping("/lectures/{lectureSeq}")
    public ResponseEntity<String> createVideo(@PathVariable long lectureSeq, VideoFormDto videoFormDto) throws IOException {

        try {
            Video video = Video.createVideo(videoFormDto, videoService.findLecture(lectureSeq));

            // 이미지 업로드
            String videoName = fileService.saveFile(videoFormDto.getVideoContent(), video.getVideoContent());
            String dataName = fileService.saveFile(videoFormDto.getVideoLectureData(), video.getVideoLectureData());

            video.setVideoContent(videoName);
            video.setVideoLectureData(dataName);
            // DB 저장
            videoRepository.save(video);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("영상 등록 완료");
    }


    // 영상 수정
    @PutMapping("/lectures/{lectureSeq}/video/{videoSeq}")
    public ResponseEntity<String> updateVideo(@PathVariable long videoSeq, VideoFormDto videoFormDto) throws IOException {

        try {
            Video video = videoService.findModifyVideo(videoSeq);
            video.setVideoContent(video.getVideoContent().substring(0, video.getVideoContent().lastIndexOf(".")));
            video.setVideoLectureData(video.getVideoLectureData().substring(0, video.getVideoLectureData().lastIndexOf(".")));
            Video.modifyVideo(videoFormDto, video, videoSeq);

            String videoName = fileService.saveFile(videoFormDto.getVideoContent(), video.getVideoContent());
            String dataName = fileService.saveFile(videoFormDto.getVideoLectureData(), video.getVideoLectureData());

            video.setVideoContent(videoName);
            video.setVideoLectureData(dataName);
            videoRepository.save(video);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.OK).body("영상 변경 완료");
    }


    // 영상 삭제
    @DeleteMapping("/lectures/{lectureSeq}/video/{videoSeq}")
    public ResponseEntity<String> deleteVideo(@PathVariable long videoSeq) {

        try {
            Video video = videoService.findDeleteVideo(videoSeq);
            fileService.removeFile(video.getVideoContent());
            fileService.removeFile(video.getVideoLectureData());
            videoRepository.delete(video);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.OK).body("영상 삭제 완료");
    }



    // 질의응답 메인 페이지
    @GetMapping ("/lectures/{lectureSeq}/video/{videoSeq}")
    public ResponseEntity<List<Inquiry>> printInquiry(@PathVariable long videoSeq) {

        List<Inquiry> inquiryList = inquiryService.findVideoInquiryList(videoSeq);

        return ResponseEntity.ok(inquiryList);
    }


    // 답변 확인
    @GetMapping ("/lectures/{lectureSeq}/video/{videoSeq}/inquiry/{inquirySeq}")
    public ResponseEntity<List<InquiryAnswer>> printInquiryAnswer(@PathVariable long inquirySeq) {

        List<InquiryAnswer> inquiryAnswerList=inquiryAnswerRepository.findByInquiry(inquiryRepository.findByInquirySeq(inquirySeq));

        return ResponseEntity.ok(inquiryAnswerList);
    }

    // 답변 등록
    @PostMapping ("/lectures/{lectureSeq}/video/{videoSeq}/inquiry/{inquirySeq}")
    public ResponseEntity<String> createInquiryAnswer(@PathVariable long inquirySeq, String inquiryAnswerString,
                                                      @RequestHeader("Authorization")String token){

        try{
            Inquiry inquiry = inquiryRepository.findByInquirySeq(inquirySeq);
            InquiryAnswer inquiryAnswer = InquiryAnswer.createInquiryAnswer(inquiryAnswerString, memberService.findMemberByToken(token), inquiryRepository.findByInquirySeq(inquirySeq));
            inquiryAnswerRepository.save(inquiryAnswer);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("문의 답변 등록 완료");
    }

    // 답변 수정
    @PutMapping ("/lectures/{lectureSeq}/video/{videoSeq}/inquiry/{inquirySeq}/q/{inquiryAnswerSeq}")
    public ResponseEntity<String> updateInquiryAnswer(String inquiryAnswerString, @PathVariable long inquiryAnswerSeq) {

        try {
            InquiryAnswer inquiryAnswer = inquiryAnswerRepository.findByInquiryAnswerSeq(inquiryAnswerSeq);
            InquiryAnswer.updateInquiryAnswer(inquiryAnswer, inquiryAnswerString, inquiryAnswerSeq);
            inquiryAnswerRepository.save(inquiryAnswer);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.OK).body("문의 답변 수정 완료");
    }

    // 답변 삭제
    @DeleteMapping ("/lectures/{lectureSeq}/video/{videoSeq}/inquiry/{inquirySeq}/q/{inquiryAnswerSeq}")
    public ResponseEntity<String> deleteInquiryAnswer(@PathVariable long inquiryAnswerSeq) {

        try {
            InquiryAnswer inquiryAnswer = inquiryAnswerRepository.findByInquiryAnswerSeq(inquiryAnswerSeq);
            inquiryAnswerRepository.delete(inquiryAnswer);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.OK).body("문의 답변 삭제 완료");
    }


}
