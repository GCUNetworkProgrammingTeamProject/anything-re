package com.anything.gradproject.repository;

import com.anything.gradproject.entity.VideoAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysis, Long> {
    Optional<VideoAnalysis> findByMember_UserSeqAndVideo_VideoSeq(long UserSeq, long VideoSeq);

}
