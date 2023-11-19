package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tb_video_timeline")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoTimeline {
    @Id
    private long videoTimelineSeq;

    @Column
    private String videoTimeline;

    @Column
    private String videoQuizQuestion;

    @Column
    private String videoQuizAnswer;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "video_seq")
    private Video video;
}
