package com.anything.gradproject.entity;


import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.dto.LecturesFormDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;


@Entity
@Table(name = "tb_lecture")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Lectures extends BaseEntity{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long lectureSeq; // 강의 번호

    @Column
    private String lectureName; // 강의 이름

    @Column
    private String lectureContent; // 강의 설명

    @Column
    private int lectureIndex; // 강의 목차

    @Column
    private int lecturePrice; // 강의 가격

    @Column
    private String lectureImage; // 강의 썸네일

    @Column
    private boolean lectureRecommend; // 강의 추천

    @Column
    private double lectureScore;

    @Enumerated(EnumType.STRING)
    private LecturesType lecturesType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_seq")
    private Member member;




    @Builder
    public Lectures (LecturesFormDto dto, Member member, LecturesType type) {
        this.lectureContent = dto.getLectureContent();
        this.lectureIndex = dto.getLectureIndex();
        this.lecturePrice = dto.getLecturePrice();
        this.lectureName = dto.getLectureName();
        this.member = member;
        this.lectureRecommend = false;
        this.lectureScore = 0.0;
        this.lecturesType = type;
    }


    public static Lectures createLectures(LecturesFormDto lecturesFormDto, Member member) {

        Lectures lectures = new Lectures();

        String code = UUID.randomUUID().toString();
        lectures.setLectureName(lecturesFormDto.getLectureName());
        lectures.setLectureContent(lecturesFormDto.getLectureContent());
        lectures.setLectureIndex(lecturesFormDto.getLectureIndex());
        lectures.setLecturePrice(lecturesFormDto.getLecturePrice());
        lectures.setLectureScore(0);
        lectures.setMember(member);
        lectures.setLectureImage("Lectures" + code);
        lectures.setLectureRecommend(false);

        return lectures;
    }


    public static Lectures modifyLectures(LecturesFormDto lecturesFormDto, Lectures lectures) {


        lectures.setLectureName(lecturesFormDto.getLectureName());
        lectures.setLectureContent(lecturesFormDto.getLectureContent());
        lectures.setLectureIndex(lecturesFormDto.getLectureIndex());
        lectures.setLecturePrice(lecturesFormDto.getLecturePrice());


        return lectures;
    }

}
