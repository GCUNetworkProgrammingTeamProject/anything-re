package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_per_video_analysis_detail")
@NoArgsConstructor
public class PerVideoAnalysisDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int perVideoAnalysisDetailSeq;

    private String timeline;
    private String concentration;
    @ManyToOne
    @JoinColumn(name = "per_video_analysis_seq")
    private PerVideoAnalysis perVideoAnalysis;
}
