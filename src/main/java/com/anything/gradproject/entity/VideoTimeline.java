package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @JoinColumn(name = "video_seq")
    private Video video;
}
