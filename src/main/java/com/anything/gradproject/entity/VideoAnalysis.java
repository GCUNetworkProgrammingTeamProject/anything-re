package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "tb_video_analysis")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoAnalysis extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long videoAnalysisSeq; // 분석표 번호

    @OneToMany(mappedBy = "videoAnalysis")
    private List<VideoAnalysisDetail> videoAnalysisDetails;

    @OneToOne
    @JoinColumn(name = "video_seq")
    private Video video; // fk

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Member member; // fk

}
