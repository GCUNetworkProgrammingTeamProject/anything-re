package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "video_analysis_seq")
    private VideoAnalysis videoAnalysis;

    @Builder
    public VideoAnalysisDetail(int timeline, float concentration, VideoAnalysis videoAnalysis) {
        this.timeline = timeline;
        this.concentration = concentration;
        this.videoAnalysis = videoAnalysis;
    }


}
