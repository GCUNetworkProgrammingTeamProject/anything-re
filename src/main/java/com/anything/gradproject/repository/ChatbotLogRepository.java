package com.anything.gradproject.repository;

import com.anything.gradproject.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {
    Optional<ChatbotLog> findByVideo_VideoSeqAndMember_UserSeq(long videoSeq, long userSeq);
}
