package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
@Table(name = "tb_chatbot_log_detail")
public class ChatbotLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatbotLogDetailSeq;

    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "chatbot_log_seq")
    private ChatbotLog chatbotLog;
}
