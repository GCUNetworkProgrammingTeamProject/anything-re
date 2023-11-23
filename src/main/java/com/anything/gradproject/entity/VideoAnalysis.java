package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(mappedBy = "videoAnalysis", orphanRemoval = true)
    private List<VideoAnalysisDetail> videoAnalysisDetails;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "video_seq")
    private Video video; // fk

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member; // fk

    @Builder
    public VideoAnalysis(Video video, Member member) {
        this.video = video;
        this.member = member;
    }

}
