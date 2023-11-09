package com.anything.gradproject.repository;

import com.anything.gradproject.entity.ChatbotLogDetail;
import com.anything.gradproject.entity.PerChatbotLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerChatbotLogDetailRepository extends JpaRepository<PerChatbotLogDetail, Long> {
    List<PerChatbotLogDetail> findByPerChatbotLog_PerChatbotLogSeq(long logSeq);
}
