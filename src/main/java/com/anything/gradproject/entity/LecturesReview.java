package com.anything.gradproject.entity;


import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.dto.LectureReviewFormDto;
import com.anything.gradproject.dto.LecturesFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;


@Entity
@Table(name = "tb_lecture_review")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LecturesReview extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long revSeq;

    private String revContent;

    private int revScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "lectureSeq")
    private Lectures lectures;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "userSeq")
    private Member member;

    @Builder
    public static LecturesReview createLectureReview(LectureReviewFormDto lectureReviewFormDto, Lectures lectures, Member member) {

        LecturesReview lecturesReview = new LecturesReview();

        lecturesReview.setLectures(lectures);
        lecturesReview.setMember(member);
        lecturesReview.setRevScore(lectureReviewFormDto.getRevScore());
        lecturesReview.setRevContent(lectureReviewFormDto.getRevContent());

        return lecturesReview;
    }

    public static LecturesReview modifyLectureReview(LectureReviewFormDto lectureReviewFormDto, long revSeq){
        LecturesReview lecturesReview = new LecturesReview();

        lecturesReview.setRevSeq(revSeq);
        lecturesReview.setRevScore(lectureReviewFormDto.getRevScore());
        lecturesReview.setRevContent(lectureReviewFormDto.getRevContent());

        return lecturesReview;
    }

}
