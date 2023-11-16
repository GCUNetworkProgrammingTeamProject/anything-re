package com.anything.gradproject.service;

import com.anything.gradproject.dto.InquiryFormDto;
import com.anything.gradproject.dto.InquiryResponseDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    @Transactional
    public void updateInquiry(Long inquirySeq, InquiryFormDto dto){
        Inquiry inquiry = inquiryRepository.findByInquirySeq(inquirySeq).orElseThrow(()->new IllegalArgumentException("해당 질문이 존재 하지 않습니다."));
        try {
            if (dto.getInquiryTitle() != null) {
                inquiry.setInquiryTitle(dto.getInquiryTitle());
            } else if (dto.getInquiryQuestion() != null) {
                inquiry.setInquiryQuestion(dto.getInquiryQuestion());
            }
        } catch (Exception e) {
            throw new RuntimeException("수정 중 오류가 발생했습니다. 다시 시도해주세요");
        }

    }

    public Inquiry findDeleteInquiry(Long inquirySeq){
        Inquiry inquiry = inquiryRepository.findByInquirySeq(inquirySeq).orElseThrow(()->new IllegalArgumentException("해당 질문이 존재 하지 않습니다."));
        return inquiry;
    }


    public List<Inquiry> findVideoInquiryList(Long videoSeq){
        return inquiryRepository.findByVideo(videoRepository.findByVideoSeq(videoSeq).get());

    }


    public List<InquiryAnswer> answerList(Inquiry inquiry){
        List<InquiryAnswer> inquiryAnswerList = inquiryAnswerRepository.findByInquiry(inquiry);
        return inquiryAnswerList;
    }


    public List<InquiryResponseDto> findAllQuery(long videoSeq) {
        List<Inquiry> queryList = inquiryRepository.findByVideo_VideoSeq(videoSeq);
        return queryList.stream().map(this::queryToDto).collect(Collectors.toList());
    }

    public InquiryResponseDto queryToDto(Inquiry inquiry) {
        return InquiryResponseDto
                .builder()
                .inquiry(inquiry)
                .build();
    }
}