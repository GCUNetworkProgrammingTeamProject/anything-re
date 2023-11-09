package com.anything.gradproject.repository;

import com.anything.gradproject.entity.TeacherDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherDetailRepository extends JpaRepository<TeacherDetail, Long> {

    Optional<TeacherDetail> findByMember_UserSeq(long userSeq);
}
