package com.anything.gradproject.repository;

import com.anything.gradproject.dto.CommunityCommentDto;
import com.anything.gradproject.entity.CommunityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {

    Optional<CommunityComment> findById(long id);
    Page<CommunityComment> findByCommunityPost_CpSeq(long id, Pageable pageable);
}
