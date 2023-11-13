package com.anything.gradproject.controller.api;

import com.anything.gradproject.dto.LoginRequestDto;
import com.anything.gradproject.dto.MemberFormDto;
import com.anything.gradproject.dto.PasswordFormDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {


//     회원인증
//     Controller
//     - 요청경로 매핑
//     회원가입 - 처리        POST    /new
//
//     로그인 - 처리         POST    /login
//
//     로그아웃 - 처리		GET		/logout

    private final MemberService memberService;

    @PostMapping("/register") // 회원가입
    public ResponseEntity<String> save(@RequestBody MemberFormDto memberFormDto) { // username, password, email
        try {
            memberService.saveMember(memberFormDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다."); // 자바오브젝트를 JSON으로 변환해서 리턴 (Jackson)
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 에러 메세지 출력
        }
    }

  //  @PostMapping("/login")
   // public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
//
  //  }

    @ResponseBody
    @GetMapping("/id") // 아이디 찾기
    public ResponseEntity<String> findId(@RequestParam String email) {
        //System.out.println(email);
        String username = memberService.findId(email);
        if (username == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디를 찾지 못했습니다.");
        return ResponseEntity.ok(username);
    }

    @GetMapping("/password") // 비밀번호 찾기
    public ResponseEntity<String> findPass(@ModelAttribute PasswordFormDto passwordFormDto) {
        //System.out.println(passwordFormDto);
        try {
            memberService.resetPassword(passwordFormDto);
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}