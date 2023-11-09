package com.anything.gradproject.repository;

import com.anything.gradproject.entity.ChatbotLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotLogRepository extends JpaRepository<ChatbotLog, Long> {
    public long findByVideo_VideoSeqAndMember_UserSeq(long videoSeq, long userSeq);
}
