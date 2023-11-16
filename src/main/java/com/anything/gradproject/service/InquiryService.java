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
    private final InquiryAnswerRepository inquiryAnswerRepository;

    public void saveInquiry(InquiryFormDto inquiryFormDto, Member member, Long videoSeq){
        Video video = videoRepository.findByVideoSeq(videoSeq).orElseThrow(() -> new IllegalArgumentException("해당 영상을 찾을 수 없습니다."));
        Inquiry inquiry = Inquiry.createInquiry(inquiryFormDto, member, video);
        inquiryRepository.save((inquiry));
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


    public List<InquiryAnswer> answerList(Inquiry inquiry){
        List<InquiryAnswer> inquiryAnswerList = inquiryAnswerRepository.findByInquiry(inquiry);
        return inquiryAnswerList;
    }

}