package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class VideoAnalysisDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int VideoAnalysisDetailSeq;

    private int timeline;
    private float concentration;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "video_analysis_seq")
    private VideoAnalysis videoAnalysis;

    @Builder
    public VideoAnalysisDetail(int timeline, float concentration, VideoAnalysis videoAnalysis) {
        this.timeline = timeline;
        this.concentration = concentration;
        this.videoAnalysis = videoAnalysis;
    }


}
