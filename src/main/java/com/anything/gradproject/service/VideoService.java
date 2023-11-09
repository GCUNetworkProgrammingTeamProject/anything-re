package com.anything.gradproject.service;

import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.dto.VideoFormDto;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import com.anything.gradproject.repository.LecturesRepository;
import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;


@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final  LecturesRepository lecturesRepository;
    private final  VideoRepository videoRepository;


    public Video findModifyVideo(Long videoSeq){
        Video video = videoRepository.findByVideoSeq(videoSeq)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 영상을 찾을 수 없습니다.");});
        return video;
    }

    public Video findDeleteVideo(Long seletedSeq){
        Video video = videoRepository.findByVideoSeq(seletedSeq)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 영상을 찾을 수 없습니다.");});
        return video;
    }

    public boolean isDuplIndex(VideoFormDto videoFormDto){ //연구대상
        // 인덱스 중복 체크
        List<Video> tmpList = videoRepository.findAll();
        Video temp = null;
        for (Video v: tmpList) {
            if (v.getVideoIndex() == videoFormDto.getVideoIndex() && v.getLectures().getLectureSeq() == videoFormDto.getLectureSeq()){
                temp = v;
                break;
            }
        }

        if (!isNull(temp)) return true;
        else return false;
    }

    public boolean isVauleEmpty(VideoFormDto videoFormDto){
        if (videoFormDto.getVideoName() == null || videoFormDto.getVideoIndex() < -1 ||
                videoFormDto.getVideoContent().isEmpty() || videoFormDto.getVideoLectureData().isEmpty())
            return  true;
        else return false;
    }

    public List<Video> findLectureVideoList(Long lectureSeq){
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
        return videoRepository.findByLectures(lectures);
    }

    public Lectures findLecture(Long lectureSeq){
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 강의를 찾을 수 없습니다.");});
        return lectures;
    }


}