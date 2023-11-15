package com.anything.gradproject.entity;


import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.dto.VideoFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "tb_video")
@Getter @Setter
@ToString
@NoArgsConstructor
public class Video extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long videoSeq; // 영상 번호

    @Column
    private String videoName; // 영상 이름

    @Column
    private String videoContent; // 영상 filePath

    @Column
    private int videoIndex; // 강의 내 영상 순서

    @Column
    private int videoLength; // 영상 총 길이(초단위)

    @Column
    private String videoLectureData; // 강의 자료

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lectureSeq")
    private Lectures lectures;

    @Builder
    public Video(String videoPath, String dataPath, Lectures lectures, LecturesFormDto dto) {

        this.videoContent = videoPath;
        this.videoIndex = 1;
        if (dto.getVideoDuration() == null) {
            this.videoLength = 1;
        } else {
            this.videoLength = dto.getVideoDuration();
        }
        this.videoLectureData = dataPath;
        this.lectures = lectures;
        this.videoName = lectures.getLectureName();

    }

    public static Video createVideo(VideoFormDto videoFormDto, Lectures lectures) {

        Video video = new Video();

        String code1 = UUID.randomUUID().toString();
        String code2 = UUID.randomUUID().toString();
        video.setVideoName(videoFormDto.getVideoName());
        video.setVideoIndex(videoFormDto.getVideoIndex());
        video.setVideoContent("Video" + code1);
        video.setVideoLength(videoFormDto.getVideoLength());
        video.setVideoLength(videoFormDto.getVideoLength());
        video.setVideoLectureData("LectureData" + code2);
        video.setLectures(lectures);

        return video;
    }


    public static Video modifyVideo(VideoFormDto videoFormDto, Video video, long videoSeq) {

        video.setVideoSeq(videoSeq);
        video.setVideoName(videoFormDto.getVideoName());
        video.setVideoIndex(videoFormDto.getVideoIndex());
        video.setVideoLength(videoFormDto.getVideoLength());

        return video;
    }
    /*@Data
    public static class videoPK implements Serializable {
        private long lectureSeq;
        private long videoSeq;
    }*/
}
