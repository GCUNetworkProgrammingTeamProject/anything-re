package com.anything.gradproject.repository;

import com.anything.gradproject.entity.VideoAnalysisDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoAnalysisDetailRepository extends JpaRepository<VideoAnalysisDetail, Integer> {
    List<VideoAnalysisDetail> findByVideoAnalysis_VideoAnalysisSeq(long vaSeq);
}
