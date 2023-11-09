package com.anything.gradproject.repository;

import com.anything.gradproject.dto.ChatbotResponseDto;
import com.anything.gradproject.entity.ChatbotLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatbotLogDetailRepository extends JpaRepository<ChatbotLogDetail, Long> {
    List<ChatbotLogDetail> findByChatbotLog_ChatbotLogSeq(long logSeq);
}
