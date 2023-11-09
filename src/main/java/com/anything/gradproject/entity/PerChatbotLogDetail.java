package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_per_chatbot_log_detail")
@NoArgsConstructor
public class PerChatbotLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perChatbotLogDetailSeq;

    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "per_chatbot_log_seq")
    private PerChatbotLog perChatbotLog;

}
