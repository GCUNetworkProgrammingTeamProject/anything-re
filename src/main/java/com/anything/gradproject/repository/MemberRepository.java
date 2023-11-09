package com.anything.gradproject.repository;

import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String id);
    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserSeq(Long userSeq);

    List<Member> findByTeacherStatus(TeacherStatus teacherStatus);

//    Optional<Member> findByMemberName(String username);
}
