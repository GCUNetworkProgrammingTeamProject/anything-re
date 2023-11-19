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
@Getter
@Setter
@ToString
@Table(name = "tb_per_chatbot_log")
@NoArgsConstructor
public class PerChatbotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perChatbotLogSeq;

    @OneToMany(mappedBy = "perChatbotLog")
    private List<PerChatbotLogDetail> perChatbotLogDetails;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member; // fk

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "personal_video_seq")
    private PersonalVideo personalVideo;
}
