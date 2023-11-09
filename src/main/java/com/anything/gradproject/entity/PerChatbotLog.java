package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @JoinColumn(name = "user_seq")
    private Member member; // fk

    @OneToOne
    @JoinColumn(name = "personal_video_seq")
    private PersonalVideo personalVideo;
}
