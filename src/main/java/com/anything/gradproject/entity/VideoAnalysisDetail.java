package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class VideoAnalysisDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int VideoAnalysisDetailSeq;

    private String timeline;
    private String concentration;

    @ManyToOne
    @JoinColumn(name = "video_analysis_seq")
    private VideoAnalysis videoAnalysis;
}
