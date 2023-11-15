package com.anything.gradproject.service;

import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.dto.VideoFormDto;
import com.anything.gradproject.dto.VideoResponseDto;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.Video;
import com.anything.gradproject.repository.LecturesRepository;
import com.anything.gradproject.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final  LecturesRepository lecturesRepository;
    private final  VideoRepository videoRepository;
    private final FileService fileService;


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


    public List<VideoResponseDto> findVideo(long lectureSeq) {
        List<Long> videoSeqList;
        List<Video> videoList = videoRepository.findByLectures_LectureSeq(lectureSeq);
        return videoList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public VideoResponseDto entityToDto(Video video) {
        VideoResponseDto dto = new VideoResponseDto();
        dto.setDuration(video.getVideoLength());
        dto.setVideoSrc(video.getVideoContent());
        dto.setVideoName(video.getVideoName());
        dto.setId(video.getVideoSeq());
        dto.setIndex(video.getVideoIndex());
        dto.setVideoLectureData(video.getVideoLectureData());
        return dto;
    }

    public void saveVideo(Lectures lectures, LecturesFormDto dto) {
        String saveVideoPath = fileService.saveFile2(dto.getVideo());
        String saveDataPath = fileService.saveFile2(dto.getData());
        Video video =new Video(saveVideoPath, saveDataPath, lectures, dto);
        videoRepository.save(video);
    }


}