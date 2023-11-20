package com.anything.gradproject.repository;

import com.anything.gradproject.entity.TeacherDeny;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherDenyRepository extends JpaRepository<TeacherDeny, Long> {
  Optional<TeacherDeny> findByDenySeq(Long denySeq);
}
