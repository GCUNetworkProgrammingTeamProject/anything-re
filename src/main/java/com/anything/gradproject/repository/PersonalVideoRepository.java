package com.anything.gradproject.repository;

import com.anything.gradproject.entity.PersonalVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalVideoRepository extends JpaRepository<PersonalVideo, Long> {
    Optional<PersonalVideo> findByMember_UserSeqAndPersonalVideoCn(long userSeq, String perVidCn);
}
