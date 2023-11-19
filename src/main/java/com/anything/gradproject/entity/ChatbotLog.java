package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "tb_chatbot_log")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatbotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatbotLogSeq; // 분석표 번호

    @OneToMany(mappedBy = "chatbotLog")
    private List<ChatbotLogDetail> chatbotLogDetails;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "video_seq")
    private Video video; // 영상 번호

    @ManyToOne
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    private Member member; // fk
}
