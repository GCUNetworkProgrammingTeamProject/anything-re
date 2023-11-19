package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity @Getter
@Table(name = "tb_chatbot_log_detail")
@NoArgsConstructor
public class ChatbotLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatbotLogDetailSeq;

    private String question;
    private String answer;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "chatbot_log_seq")
    private ChatbotLog chatbotLog;

    @Builder
    public ChatbotLogDetail(String question, String answer, ChatbotLog chatbotLog) {
        this.answer = answer;
        this.question = question;
        this.chatbotLog = chatbotLog;
    }
}
