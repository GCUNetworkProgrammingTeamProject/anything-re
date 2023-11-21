package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "personal_video_seq")
    private PersonalVideo personalVideo; // 개인 영상 번호

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member; // fk

    @Builder
    public PerVideoAnalysis(Member member, PersonalVideo personalVideo) {
        this.member = member;
        this.personalVideo = personalVideo;
    }

}
