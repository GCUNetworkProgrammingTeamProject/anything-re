package com.anything.gradproject.controller;

import com.anything.gradproject.auth.PrincipalDetail;
import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.dto.AdvertiseFormDto;
import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.Advertisement;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.PurchaseList;
import com.anything.gradproject.repository.AdvertisementRepository;
import com.anything.gradproject.repository.LecturesRepository;
import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.repository.PurchaseListRepository;
import com.anything.gradproject.service.*;
import com.anything.gradproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminController {

    private final AdvertisementRepository advertisementRepository;
    private final LecturesRepository lecturesRepository;
    private final MemberRepository memberRepository;
    private final PurchaseListRepository purchaseListRepository;
    private final MemberService memberService;
    private final LectureService lectureService;
    private final FileService fileService;
    private final TeacherDetailService teacherDetailService;
    private final PurchaseService purchaseService;

    @GetMapping("/admin/order")
    public ResponseEntity<?> printPurchaseList() {
        try {
            AdminPurchaseListDto dto = purchaseService.getPurchaseList();
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error : " + e.getMessage() + "\nError Stack Trace : " + e.getStackTrace());
        }
    }

    // 광고 리스트 출력
    @GetMapping("/admin/ad")
    public ResponseEntity<List<Advertisement>> printAdvers() {

        List<Advertisement> adversList = advertisementRepository.findAll();
        return ResponseEntity.ok(adversList);
    }

    // 광고 등록
    @PostMapping("/admin/ad")
    public ResponseEntity<String> createAdvers(AdvertiseFormDto advertiseFormDto) throws IOException {

        try {
            // 초기화
            Advertisement advertisement = Advertisement.createAdvertisement(advertiseFormDto);
            // 이미지 저장
            String imageName = fileService.saveFile(advertiseFormDto.getAdverImage(), advertisement.getAdverImage());
            advertisement.setAdverImage(imageName);
            // DB 저장
            advertisementRepository.save(advertisement);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("광고 등록 완료");
    }

    // 광고 수정
    @PutMapping("/admin/ad/{adverSeq}")
    public ResponseEntity<String> updateAdvers(@PathVariable long adverSeq, AdvertiseFormDto advertiseFormDto)
            throws IOException {
        try {
            Advertisement advertisement = advertisementRepository.findByadverSeq(adverSeq);
            advertisement.setAdverImage(
                    advertisement.getAdverImage().substring(0, advertisement.getAdverImage().lastIndexOf(".")));
            Advertisement.modifyAdvertisement(advertiseFormDto, advertisement);
            String imageName = fileService.saveFile(advertiseFormDto.getAdverImage(), advertisement.getAdverImage());
            advertisement.setAdverImage(imageName);
            advertisementRepository.save(advertisement);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("광고 변경 완료");
    }

    // 광고 삭제
    @DeleteMapping("/admin/ad/{adverSeq}")
    public ResponseEntity<String> deleteAdvers(@PathVariable int adverSeq) {
        try {
            long Seq = Long.valueOf(adverSeq);
            Advertisement advertisement = advertisementRepository.findByadverSeq(Seq);
            fileService.removeFile(advertisement.getAdverImage());
            advertisementRepository.delete(advertisement);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("광고 삭제 완료");
    }

    // 메인 배너에 올릴 광고 선택
    @PostMapping("/admin/ad/select")
    public ResponseEntity<String> selectMainAdvers(@RequestBody AdvertiseRequestDto dto) {
        try {
            Advertisement advertisements = advertisementRepository.findByadverSeq(dto.getAdverSeq());
            if (dto.getIsBanner() == 0)
                advertisements.setBanner(false);
            else if (dto.getIsBanner() == 1)
                advertisements.setBanner(true);
            advertisementRepository.save(advertisements);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("배너 광고 변경 완료");
    }

    // 배너 광고 리스트 출력
    @GetMapping("/admin/ad/rec")
    public ResponseEntity<List<Advertisement>> printRecAdvers() {

        List<Advertisement> AdverList = advertisementRepository.findByIsBanner(true);
        List<Advertisement> recAdverList = new ArrayList<>();
        if (AdverList.get(0) != null)
            recAdverList.add(AdverList.get(0));
        if (AdverList.get(1) != null)
            recAdverList.add(AdverList.get(1));
        if (AdverList.get(2) != null)
            recAdverList.add(AdverList.get(2));
        return ResponseEntity.ok(recAdverList);
    }

    // 강의 목록 출력
    @GetMapping("/admin/lectures")
    public ResponseEntity<?> printLectures(@RequestHeader("Authorization") String token) {
        try {
            List<Lectures> lecturesList = lectureService.findUserLectureList(memberService.findMemberByToken(token));
            return ResponseEntity.ok(lecturesList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // 추천 강의 목록 출력
    @GetMapping("/admin/lectures/rec")
    public ResponseEntity<List<Lectures>> printRecLectures() {
        List<Lectures> lecturesList = lecturesRepository.findByLectureRecommend(true);
        return ResponseEntity.ok(lecturesList);
    }

    // 추천 강의 등록
    @PostMapping("/admin/lectures/rec/{lectureSeq}")
    public ResponseEntity<String> addRecLectures(@PathVariable long lectureSeq) {
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
        lectures.setLectureRecommend(true);
        lecturesRepository.save(lectures);
        return ResponseEntity.status(HttpStatus.CREATED).body("추천 강의 등록 완료");
    }

    // 추천 강의 삭제
    @DeleteMapping("/admin/lectures/rec/{lectureSeq}")
    public ResponseEntity<String> deleteRecLectures(@PathVariable long lectureSeq) {
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
        lectures.setLectureRecommend(false);
        lecturesRepository.save(lectures);
        return ResponseEntity.status(HttpStatus.CREATED).body("추천 강의 삭제 완료");
    }

    @GetMapping("/admin/Teacher") // 강사등록신청 조회
    public ResponseEntity<List<TeacherDetailResponseDto>> getTeacherDetail() {

        List<TeacherDetailResponseDto> dtoList = teacherDetailService.getAllTeacherDetail();

        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PutMapping("/admin/Teacher") // reason, teacherDetailSeq, status(0,1둘중하나 long 타입) 세개 받아와야함.
    public ResponseEntity<String> setTeacherStatus(@RequestBody TeacherDenyDto dto) {
        teacherDetailService.setTeacherDetail(dto);

        return ResponseEntity.status(HttpStatus.OK).body("적용이 완료 되었습니다.");
    }

    @GetMapping("/admin/users")
    public ResponseEntity<Page<MemberResponseDto>> findAllMember(
            @PageableDefault(size = 300, sort = "userSeq", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<MemberResponseDto> memberPage = memberService.findAll(pageable);
        return new ResponseEntity<>(memberPage, HttpStatus.OK);
    }

    @PatchMapping("/admin/users/{userSeq}")
    public ResponseEntity<String> adminUpdateMember(@PathVariable long userSeq, @RequestBody MemberUpdateDto dto)
            throws Exception {
        try {
            memberService.partiallyUpdate(dto, userSeq);
            return ResponseEntity.status(HttpStatus.OK).body("정보 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/admin/checkUsers")
    public String printUsers() {
        String result = "";
        result = memberService.checkUsers();
        return result;
    }

}
