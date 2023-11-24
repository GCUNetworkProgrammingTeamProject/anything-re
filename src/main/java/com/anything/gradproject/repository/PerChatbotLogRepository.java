package com.anything.gradproject.repository;

import com.anything.gradproject.entity.PerChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerChatbotLogRepository extends JpaRepository<PerChatbotLog, Long> {
    public long findByPersonalVideo_PersonalVideoSeqAndMember_UserSeq(long perVideoSeq, long UserSeq);

    Optional<PerChatbotLog> findByPersonalVideo_PersonalVideoCnAndMember_UserSeq(String VideoCn, long userSeq);
}
