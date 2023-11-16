package com.anything.gradproject.service;

import com.anything.gradproject.config.JwtTokenProvider;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.token.JwtToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public static MemberInfoDto getInfo(Member memberByToken) {
        MemberInfoDto dto = new MemberInfoDto();
        dto.setRole(memberByToken.getRole().toString());
        dto.setName(memberByToken.getName());
        dto.setEmail(memberByToken.getEmail());
        return dto;
    }


    public JwtToken login(String id, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    public void saveMember(MemberFormDto memberFormDto) {

        String duplicateIdMessage = checkDuplicateId(memberFormDto.getId());
        String duplicateEmailMessage = checkDuplicateMemberEmail(memberFormDto.getEmail());

        if (duplicateIdMessage != null) {
            throw new IllegalStateException(duplicateIdMessage);
        }

        if (duplicateEmailMessage != null) {
            throw new IllegalStateException(duplicateEmailMessage);
        }
        Member member = Member.builder()
                .memberFormDto(memberFormDto)
                .passwordEncoder(passwordEncoder)
                .build();
        memberRepository.save(member);

    }

    //아이디 중복 확인
    public String checkDuplicateId(String id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (!findMember.isEmpty()) {
            return "중복된 아이디입니다.";
        }
        return null;
    }

    //이메일 중복 확인
    public String checkDuplicateMemberEmail(String email) {
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        if (!findEmail.isEmpty()) {
            return "중복된 이메일입니다.";
        }
        return null;
    }

    //아이디 찾기
    public String findId(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다.");});
        return member.getId();
    }

    // 개인정보 수정
    @Transactional
    public void partiallyUpdate(MemberUpdateDto memberUpdateDto, long userSeq) {

        Member persistence = memberRepository.findById(userSeq).orElseThrow(() ->{
            return new IllegalArgumentException("회원 찾기 실패");
        });
        try {
            if (memberUpdateDto.getName() != null) {
                persistence.setName(memberUpdateDto.getName());
            }
            if (memberUpdateDto.getEmail() != null) {
                persistence.setEmail(memberUpdateDto.getEmail());
            }
            if (memberUpdateDto.getPassword() != null) {
                String rawPassword = memberUpdateDto.getPassword();
                String encPassword = passwordEncoder.encode(rawPassword);
                persistence.setPassword(encPassword);
            }
        } catch (Exception e) {
            throw new RuntimeException("회원 정보 업데이트 중 오류가 발생했습니다.");
        }
    }

    public List<Member> seletedTeacherList(List<Long> selectedList){
        List<Member> memberList = null;
        for (Long l:selectedList)
            memberList.add(memberRepository.findByUserSeq(l).get());
        return memberList;
    }

    @Transactional
    public void resetPassword(PasswordFormDto passwordFormDto) {
        Member member = memberRepository.findById(passwordFormDto.getMemberId()).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );

        if (member.getName().equals(passwordFormDto.getMemberName()) &&
                member.getEmail().equals(passwordFormDto.getMemberEmail())) {
            String newPassword = passwordEncoder.encode(passwordFormDto.getPassword());
            member.setPassword(newPassword);
        } else {
            throw new IllegalArgumentException("입력된 정보가 올바르지 않습니다."); // 예외 던지기
        }
    }

    public MemberResponseDto convertToDto(Member member) {
        MemberResponseDto dto = new MemberResponseDto(member);
        return dto;
    }
    public Page<MemberResponseDto> findAll(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return memberPage.map(this::convertToDto);
    }

    public Member findMember(String userId) {
        Member member = memberRepository.findById(userId).orElseThrow(()->{
            throw new IllegalArgumentException("해당 아이디가 없습니다.");});
        return member;
    }

    public Member findMemberByToken(String token) {
        String jwtToken = token.replace("Bearer ", ""); // Bearer 부분 제거
        UserDetails userDetails = jwtTokenProvider.extractUserIdFromToken(jwtToken);
        String userId = userDetails.getUsername();
        Member member = memberRepository.findById(userId).orElseThrow(()->{
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다.");});
        return member;
    }

public String checkUsers(){
        String [] command = {"/bin/sh","-c", "netstat -nap | grep :60009 | grep ESTABLISHED | wc -l"};
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        try {
            // Run script
            Process process = processBuilder.start();

            // Read output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            if(output.toString().equals("")) {
                result = "success";
            }else {
                result = output.toString();
            }
        } catch (Exception e) {
            result = "fail";
        }
        return result;
    }
}