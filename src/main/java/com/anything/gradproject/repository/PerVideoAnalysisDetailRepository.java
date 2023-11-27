package com.anything.gradproject.repository;

import com.anything.gradproject.entity.PerVideoAnalysisDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerVideoAnalysisDetailRepository extends JpaRepository<PerVideoAnalysisDetail, Integer> {
    List<PerVideoAnalysisDetail> findByPerVideoAnalysis_PerVideoAnalysisSeq(long perVideoAnalysisSeq);
}
