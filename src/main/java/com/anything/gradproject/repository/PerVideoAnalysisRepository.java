package com.anything.gradproject.repository;

import com.anything.gradproject.entity.PerVideoAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerVideoAnalysisRepository extends JpaRepository<PerVideoAnalysis, Long> {
    Optional<PerVideoAnalysis> findByPersonalVideo_PersonalVideoSeq(long personalVideoSeq);

    List<PerVideoAnalysis> findByMember_UserSeq(long userSeq);
}
