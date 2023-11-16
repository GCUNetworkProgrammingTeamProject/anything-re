package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Inquiry;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByVideo(Video video);

    List<Inquiry> findByVideoAndMember (Video video, Member member);

    List<Inquiry> findByVideoAndInquiryIsSecret(Video video, boolean secret);

    Optional<Inquiry> findByInquirySeq(Long inquirySeq);

    List<Inquiry> findByVideo_VideoSeq(long videoSeq);
}
