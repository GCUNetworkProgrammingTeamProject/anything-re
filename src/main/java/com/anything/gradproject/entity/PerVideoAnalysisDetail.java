package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "tb_per_video_analysis_detail")
@NoArgsConstructor
public class PerVideoAnalysisDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int perVideoAnalysisDetailSeq;

    private int timeline;
    private float concentration;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "per_video_analysis_seq")
    private PerVideoAnalysis perVideoAnalysis;

    @Builder
    public PerVideoAnalysisDetail(int key, float concentration, PerVideoAnalysis perVideoAnalysis) {
        this.timeline = key;
        this.concentration = concentration;
        this.perVideoAnalysis = perVideoAnalysis;
    }
}
