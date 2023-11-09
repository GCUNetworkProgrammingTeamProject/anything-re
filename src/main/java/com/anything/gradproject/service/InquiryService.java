package com.anything.gradproject.service;

import com.anything.gradproject.dto.InquiryFormDto;
import com.anything.gradproject.dto.VideoFormDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;


@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;

    public Inquiry saveInquiry(InquiryFormDto inquiryFormDto, Long videoSeq){
        Member member = findMember();
        Video video = videoRepository.findByVideoSeq(videoSeq).get();
        Inquiry inquiry = Inquiry.createInquiry(inquiryFormDto, member, video);
        return inquiryRepository.save((inquiry));
    }

    public Inquiry findModifyInquiry(Long inquirySeq){

        return inquiryRepository.findByInquirySeq(inquirySeq);
    }

    public Inquiry findDeleteInquiry(Long inquirySeq){

        return inquiryRepository.findByInquirySeq(inquirySeq);
    }


    public List<Inquiry> findVideoInquiryList(Long videoSeq){
        return inquiryRepository.findByVideo(videoRepository.findByVideoSeq(videoSeq).get());

    }


    public Member findMember(){
        // 현재 로그인한 계정의 id를 알아내는 과정
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String userid = ((UserDetails) principal).getUsername();

        // 알아낸 id를 통해 계정 번호를 알아냄
        Member member = memberRepository.findById(userid)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다.");});
        return member;
    }

    public List<InquiryAnswer> answerList(Inquiry inquiry){
        List<InquiryAnswer> inquiryAnswerList = inquiryAnswerRepository.findByInquiry(inquiry);
        return inquiryAnswerList;
    }

}