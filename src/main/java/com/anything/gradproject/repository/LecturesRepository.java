package com.anything.gradproject.repository;

import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LecturesRepository extends JpaRepository<Lectures, Long> {
    Optional<Lectures> findBylectureSeq(long lectureSeq);
    Optional<List<Lectures>> findByMember_UserSeq(long userSeq);
    List<Lectures> findByLectureRecommend(boolean lectureRecommend);

    Optional<Lectures> findByLecturesType(LecturesType lecturesType);

}
