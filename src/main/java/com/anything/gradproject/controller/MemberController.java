package com.anything.gradproject.controller;

import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import com.anything.gradproject.service.*;
import com.anything.gradproject.token.JwtToken;
import jakarta.validation.Valid;
import com.anything.gradproject.token.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    //     회원인증
//     Controller
//     - 요청경로 매핑
//     회원가입 - 화면        GET 	/new                    -> member/register.html 반환
//     회원가입 - 처리        POST    /new
//
//     아이디찾기 - 화면		GET 	/id?email={email}       -> /community/postForm.html 반환
//
//     로그인 - 화면         GET     /login                  -> /member/login.html 반환
//     로그인 - 처리         POST    /login
//
//     로그아웃 - 처리		GET		/logout
//
//
    private final LecturesRepository lecturesRepository;
    private final LectureService lectureService;
    private final ShoppingService shoppingService;
    private final ShoppingListRepository shoppingListRepository;
    private final PurchaseService purchaseService;
    private final PurchaseListRepository purchaseListRepository;
    private final InquiryRepository inquiryRepository;
    private final VideoRepository videoRepository;
    private final InquiryService inquiryService;
    private final InquiryAnswerRepository inquiryAnswerRepository;
    private final LectureReviewRepository lectureReviewRepository;
    private final FileService fileService;
    private final AnalysisService analysisService;
    private final ChatGptService chatGptService;
    private final MemberService memberService;


    @PostMapping("/login") //로그인
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody LoginRequestDto dto) {
        // System.out.println("로그인 요청 도착");
        JwtToken token = memberService.login(dto.getId(), dto.getPassword());
        return ResponseEntity.ok(token);
    }
//    @GetMapping("/loginCheck") //
//    public ResponseEntity<Member> loginCheck(@RequestHeader("Authorization") String token) {
//
//        return ResponseEntity.status(HttpStatus.OK).body(memberService.findMemberByToken(token));
//    }
    @GetMapping("/info") // 로그인 유저 정보 반환
    public ResponseEntity<MemberInfoDto> getInfo(@RequestHeader("Authorization") String token) {
        MemberInfoDto dto = MemberService.getInfo(memberService.findMemberByToken(token));
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // 강의 상세 정보 출력
    @GetMapping(value = "/lectures/detail/{lectureSeq}")
    public ResponseEntity<LectureResponseDto> printLectureDetail(@PathVariable long lectureSeq) {
        LectureResponseDto dto = lectureService.getLectureDetail(lectureSeq);
        // System.out.println(dto.getAuthorName()+dto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    // 구매한 강의 목록 출력
    @GetMapping(value = "/users/lectures")
    public ResponseEntity<List<Lectures>> printPurchaseLectures(@RequestHeader("Authorization")String token) {

        List<Lectures> lecturesList = purchaseService.findLecutresByMember(memberService.findMemberByToken(token));
        return ResponseEntity.ok(lecturesList);
    }

    // 구매한 강의의 타입별 검색
    @GetMapping(value = "/users/lectures/search/{lectureType}")
    public ResponseEntity<List<Lectures>> printPurchaseType(@PathVariable String lectureType,
                                                                @RequestHeader("Authorization")String token) {
        LecturesType lecturesType = lectureService.setLecturesType(lectureType);
        List<PurchaseList> purchaseLists = purchaseListRepository.findByMemberAndLectures(memberService.findMemberByToken(token),
                lecturesRepository.findByLecturesType(lecturesType).get());
        List<Lectures> lecturesList = null;
        for (PurchaseList p: purchaseLists)
            lecturesList.add(p.getLectures());

        return ResponseEntity.ok(lecturesList);
    }


    // 구매한 강의의 영상 목록 출력
    @GetMapping(value = "/users/lectures/{lectureSeq}")
    public ResponseEntity<List<Video>> printPurchaseVideo(@PathVariable long lectureSeq) {
        List<Video> videoList = purchaseService.findVideoByLectures(lecturesRepository.findBylectureSeq(lectureSeq).get());

        return ResponseEntity.ok(videoList);
    }


    // 영상 시청
    @GetMapping(value = "/users/lectures/stream/{videoSeq}")
    public ResponseEntity<String> sendVideoContent(@PathVariable long videoSeq){
        Video video = videoRepository.findByVideoSeq(videoSeq).get();
        return ResponseEntity.ok(video.getVideoContent());
    }


    // 영상의 강의 자료 내려 받을 URL 값 전송
    @GetMapping(value = "/users/lectures/download/{videoSeq}")
    public ResponseEntity<String> downloadLectureData(@PathVariable long videoSeq){
        Video video = videoRepository.findByVideoSeq(videoSeq).get();
        return ResponseEntity.ok(video.getVideoLectureData());
    }


    // 영상의 문의 목록 출력
    @GetMapping(value = "/users/lectures/{lectureSeq}/{videoSeq}")
    public ResponseEntity<List<Inquiry>> printPurchaseInquiry(@PathVariable long videoSeq, @RequestHeader("Authorization")String token){
        List<Inquiry> inquiryList = inquiryRepository.findByVideoAndMember(videoRepository.findByVideoSeq(videoSeq).get(),
                memberService.findMemberByToken(token));
        List<Inquiry> listForAdd = inquiryRepository.findByVideoAndInquiryIsSecret(videoRepository.findByVideoSeq(videoSeq).get(), true);
        inquiryList.addAll(listForAdd);

        return ResponseEntity.ok(inquiryList);
    }


    // 문의 등록
    @PostMapping(value = "/users/lectures/{lectureSeq}/{videoSeq}")
    public ResponseEntity<String> createPurchaseInquiry(@PathVariable long videoSeq, InquiryFormDto inquiryFormDto, @RequestHeader("Authorization")String token){

        try{
            Inquiry inquiry = Inquiry.createInquiry(inquiryFormDto, memberService.findMemberByToken(token), videoRepository.findByVideoSeq(videoSeq).get());
            // DB 저장
            inquiryRepository.save(inquiry);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("문의 등록 완료");

    }


    // 문의 수정
    @PutMapping(value = "/users/lectures/{lectureSeq}/{videoSeq}")
    public ResponseEntity<String> updatePurchaseInquiry(@PathVariable long videoSeq, InquiryFormDto inquiryFormDto){
        try {
            Inquiry inquiry = inquiryService.findModifyInquiry(videoSeq);
            Inquiry.modifyInquiry(inquiryFormDto, inquiry);
            inquiryRepository.save(inquiry);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }

        return ResponseEntity.status(HttpStatus.OK).body("문의 수정 완료");
    }

    // 문의 삭제
    @DeleteMapping(value = "/users/lectures/{lectureSeq}/{videoSeq}/{inquirySeq}")
    public ResponseEntity<String> deletePurchaseInquiry(@PathVariable long inquirySeq) {
        try {
            Inquiry inquiry = inquiryService.findDeleteInquiry(inquirySeq);
            inquiryRepository.delete(inquiry);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("문의 삭제 완료");
    }





    // 문의 답변 목록 출력
    @GetMapping (value = "/users/lectures/{lectureSeq}/{videoSeq}/{inquirySeq}")
    public ResponseEntity<List<InquiryAnswer>> printInquiryAnswer(@PathVariable long inquirySeq) {

        List<InquiryAnswer> inquiryAnswerList = inquiryAnswerRepository.findByInquiry(inquiryRepository.findByInquirySeq(inquirySeq));
        return ResponseEntity.ok(inquiryAnswerList);
    }

    // 강의 평가 출력
    @GetMapping(value = "/users/lectures/review/{lectureSeq}")
    public ResponseEntity<List<LecturesReview>> printLectureReview(@PathVariable long lectureSeq){
        List<LecturesReview> lecturesReviews = lectureReviewRepository.findByLectures(lecturesRepository.findBylectureSeq(lectureSeq).get());
        return ResponseEntity.ok(lecturesReviews);
    }


    // 강의 평가 등록
    @PostMapping(value = "/users/lectures/review/{lectureSeq}")
    public ResponseEntity<String> createLectureReview(LectureReviewFormDto lectureReviewFormDto, @PathVariable long lectureSeq,
                                                      @RequestHeader("Authorization")String token){
        try {
            LecturesReview lecturesReview = LecturesReview.createLectureReview(lectureReviewFormDto,
                    lecturesRepository.findBylectureSeq(lectureSeq).get(), memberService.findMemberByToken(token));
            lectureReviewRepository.save((lecturesReview));
            lectureService.updateLectureScore(lectureSeq);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("강의 리뷰 등록 완료");
    }


    // 강의 평가 수정
    @PutMapping(value = "/users/lectures/review/{lectureSeq}/{revSeq}")
    public ResponseEntity<String> updateLectureReview(LectureReviewFormDto lectureReviewFormDto,@PathVariable long lectureSeq, @PathVariable long revSeq){
        try {
            LecturesReview lecturesReview = LecturesReview.modifyLectureReview(lectureReviewFormDto, revSeq);
            lectureReviewRepository.save(lecturesReview);
            lectureService.updateLectureScore(lectureSeq);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("강의 리뷰 수정 완료");
    }


    // 강의 평가 삭제
    @DeleteMapping(value = "/users/lectures/review/{lectureSeq}/{revSeq}")
    public ResponseEntity<String> deleteLectureReview(@PathVariable long lectureSeq, @PathVariable long revSeq){
        try {
            LecturesReview lecturesReview = lectureReviewRepository.findByRevSeq(revSeq);
            lectureReviewRepository.delete((lecturesReview));
            lectureService.updateLectureScore(lectureSeq);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("강의 리뷰 삭제 완료");
    }




    // 구매 목록 출력
    @GetMapping(value = "/users/order")
    public ResponseEntity<List<Lectures>> printPurchaseList(){
        //List<Lectures> lecturesList = null; //기존 코드
        List<Lectures> lecturesList = new ArrayList<>(); //수정한 코드
        List<PurchaseList> purchaseLists = purchaseListRepository.findAll();
        for (PurchaseList p: purchaseLists)
            lecturesList.add(p.getLectures());
        return ResponseEntity.ok(lecturesList);
    }






    // 장바구니에 담을 강의 선택을 위한 전체 강의 출력
    @GetMapping(value = "/users/shoplist")
    public ResponseEntity<List<LectureResponseDto>> printShopping() {
        List<Lectures> lecturesList = lecturesRepository.findAll();
        List<LectureResponseDto> dtos = lecturesList.stream().map(lectureService::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }


    // 전체 강의 중 카테고리별 출력
    @GetMapping(value = "/users/shoplist/{lectureType}")
    public ResponseEntity<List<Lectures>> printShoppingLectures(@PathVariable String lectureType) {

        LecturesType selectLecturesType = lectureService.setLecturesType(lectureType);

        List<Lectures> tmp = lecturesRepository.findAll();
        List<Lectures> lecturesList = null;
        for (Lectures l: tmp)
         if (l.getLecturesType() == selectLecturesType)
             lecturesList.add(l);
        return ResponseEntity.ok(lecturesList);
    }


    // 장바구니에 강의 담기
    @PostMapping(value = "/users/shoplist/{lectureSeq}")
    public ResponseEntity<String> createShopping(@PathVariable long lectureSeq, @RequestHeader("Authorization")String token) {
        try {
            ShoppingList shoppingList = ShoppingList.createShoppingList(lecturesRepository.findBylectureSeq(lectureSeq).get(), memberService.findMemberByToken(token));
            shoppingService.saveShoppingList(shoppingList);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("장바구니 등록 완료");
    }

    // 장바구니에서 강의 삭제
    @DeleteMapping(value = "/users/shoplist/{lectureSeq}")
    public ResponseEntity<String> deleteShopping(@PathVariable long lectureSeq, @RequestHeader("Authorization")String token) {
        try {
            ShoppingList shoppingList = shoppingService.findDeleteShoppinglist(lectureSeq, memberService.findMemberByToken(token));
            shoppingListRepository.delete(shoppingList);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 삭제 완료");
    }

    // 장바구니 목록 출력
    @GetMapping(value = "/users/shoplist/list")
    public ResponseEntity<List<Lectures>> printShoppingList(){
        List<Lectures> lecturesList = null;
        List<ShoppingList> shoppingLists = shoppingListRepository.findAll();
        for (ShoppingList s: shoppingLists)
            lecturesList.add(s.getLectures());

        return ResponseEntity.ok(lecturesList);
    }

    // 장바구니 목록 결제
    @GetMapping(value = "/users/shoplist/order")
    public ResponseEntity<String> shoppingToOrder(@RequestHeader("Authorization")String token) {

        try {
            List<ShoppingList> shoppingLists = shoppingService.findDeleteShoppinglists(memberService.findMemberByToken(token));

            for (ShoppingList s : shoppingLists) {
                PurchaseList purchaseList = new PurchaseList().createPurchaseList(s);
                purchaseListRepository.save(purchaseList);
                shoppingListRepository.delete(s);
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 리스트 구매 완료");
    }


    // 집중도 분석권 결제
    @GetMapping(value = "/users/analysis/order")
    public ResponseEntity<String> analysisToOrder() {

        try {
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
        return ResponseEntity.status(HttpStatus.OK).body("집중도 분석권 결제 완료");
    }


    @PostMapping("/user/lectures/{lectureSeq}/{videoSeq}/analysis") // (집중도 분석 요청)유저에게 녹화 파일을 받아 서버에 저장, 플라스크 서버로 전송
    public Mono<ResponseEntity<String>> sendData(@RequestHeader("Authorization")String token, @PathVariable long videoSeq, @RequestParam("file") MultipartFile file) {
        String recording = fileService.saveFile2(file);


        return analysisService.sendPostRequest(memberService.findMemberByToken(token).getUserSeq(), videoSeq, recording);
    }

    @PostMapping("/users/{videoSeq}/chatbot") // 챗봇 질문
    public Mono<String> generateChatResponse(@RequestBody String message, @PathVariable long videoSeq) {
        return chatGptService.generateChatResponse(message, videoSeq);
    }

    @PostMapping("/users/lectures/stream/per") //개인 영상 학습 종료, 정보 저장, ai전송
    public ResponseEntity<String> savePersonalStudy(
            @RequestHeader("Authorization")String token,
            @RequestBody PersonalVideoRequestDto dto,
            @RequestBody MultipartFile file) {
        dto.setMember(memberService.findMemberByToken(token));
        try {
            lectureService.savePersonalStudy(dto);
            long perVideoSeq = lectureService.findPersonalVideoSeq(
                    memberService.findMemberByToken(token).getUserSeq(),
                    dto.getPersonalVideoCn()
            );
            String recording = fileService.saveFile2(file);


            return ResponseEntity.status(HttpStatus.OK).body("영상정보 저장 완료.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/user/lectures/per/") // (집중도 분석 요청)유저에게 녹화 파일을 받아 서버에 저장, 플라스크 서버로 전송
//    public Mono<ResponseEntity<String>> sendData(@RequestHeader("Authorization")String token, @RequestParam("file") MultipartFile file) {
//        String recording = fileService.saveFile2(file);
//        long perVideoSeq = lectureService.findPersonalVideoSeq(memberService.findMemberByToken(token).getUserSeq(), String PersonalVideoCn)
//
//        return analysisService.sendPostRequest(memberService.findMemberByToken(token).getUserSeq(), videoSeq, recording);
//    }

}