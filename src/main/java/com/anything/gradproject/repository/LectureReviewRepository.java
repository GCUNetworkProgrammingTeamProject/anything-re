package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.LecturesReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureReviewRepository extends JpaRepository<LecturesReview, Long> {
    List<LecturesReview> findByLectures(Lectures lectures);

    LecturesReview findByRevSeq(Long revSeq);

}
