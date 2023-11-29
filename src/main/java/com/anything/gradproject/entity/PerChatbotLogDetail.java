package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "tb_per_chatbot_log_detail")
@NoArgsConstructor
public class PerChatbotLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perChatbotLogDetailSeq;

    private String question;
    @Lob
    private String answer;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "per_chatbot_log_seq")
    private PerChatbotLog perChatbotLog;

    @Builder
    public PerChatbotLogDetail(String question, String answer, PerChatbotLog perChatbotLog) {
        this.question = question;
        this.answer = answer;
        this.perChatbotLog = perChatbotLog;
    }

}
