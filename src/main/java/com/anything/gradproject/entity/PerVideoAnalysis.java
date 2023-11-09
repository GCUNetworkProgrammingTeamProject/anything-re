package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tb_per_video_analysis")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PerVideoAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perVideoAnalysisSeq; // 분석표 번호

    @OneToMany(mappedBy = "perVideoAnalysis")
    private List<PerVideoAnalysisDetail> perVideoAnalysisDetails;

    @OneToOne
    @JoinColumn(name = "personal_video_seq")
    private PersonalVideo personalVideo; // 개인 영상 번호

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Member member; // fk


}
