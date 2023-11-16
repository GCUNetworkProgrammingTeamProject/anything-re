package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Inquiry;
import com.anything.gradproject.entity.InquiryAnswer;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    List<InquiryAnswer> findByInquiry(Inquiry inquiry);

    InquiryAnswer findByInquiryAnswerSeq(Long inquiryAnswerSeq);

    List<InquiryAnswer> findByInquiry_InquirySeq(long inQuerySeq);
}
