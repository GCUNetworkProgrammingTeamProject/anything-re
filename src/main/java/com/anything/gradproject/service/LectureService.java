package com.anything.gradproject.service;

import com.anything.gradproject.constant.LecturesType;
import com.anything.gradproject.dto.LectureResponseDto;
import com.anything.gradproject.dto.LecturesFormDto;
import com.anything.gradproject.dto.PersonalVideoRequestDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LecturesRepository lecturesRepository;
    private final PersonalVideoRepository personalVideoRepository;
    private final LectureReviewRepository lectureReviewRepository;
    private final FileService fileService;
    private final VideoRepository videoRepository;
    private final VideoService videoService;
    private final TeacherDetailRepository teacherDetailRepository;

    public LecturesType setLecturesType(String str){
        LecturesType lecturesType;
//    MATH, ENGLISH, KOREAN, SCIENCE, SOCIAL
        switch (str) {
            case "MATH": {
                lecturesType = LecturesType.MATH;
                break;
            }
            case "ENGLISH": {
                lecturesType = LecturesType.ENGLISH;
                break;
            }
            case "KOREAN": {
                lecturesType = LecturesType.KOREAN;
                break;
            }
            case "SCIENCE": {
                lecturesType = LecturesType.SCIENCE;
                break;
            }
            default: {
                lecturesType = LecturesType.SOCIAL;
            }
        }
        return lecturesType;
    }

    public void updateLectureScore(Long lectureSeq){
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).get();
        List<LecturesReview> lecturesReviews = lectureReviewRepository.findByLectures(lectures);
        double d = 0;
        int count = 0;

        for (LecturesReview l :lecturesReviews) {
            d += l.getRevScore();
            count++;
        }

        lectures.setLectureScore(d/count);
    }

    public List<Lectures> findUserLectureList(Member member) {
        return lecturesRepository.findByMember_UserSeq(member.getUserSeq());
    }


    public void savePersonalStudy(PersonalVideoRequestDto dto) {
        PersonalVideo personalVideo = PersonalVideo.builder()
                .personalVideoCn(dto.getPersonalVideoCn())
                .member(dto.getMember())
                        .build();
        personalVideoRepository.save(personalVideo);
    }

    public long findPersonalVideoSeq(long userSeq, String personalVideoCn) {
        PersonalVideo personalVideo = personalVideoRepository.findByMember_UserSeqAndPersonalVideoCn(userSeq, personalVideoCn).orElseThrow(
                () -> new IllegalArgumentException("해당 정보를 찾을 수 없습니다.")
        );
        return personalVideo.getPersonalVideoSeq();
    }

    public LectureResponseDto getLectureDetail(long lectureSeq) {
        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).orElseThrow(()-> {
            throw new IllegalArgumentException("해당 강의가 없습니다.");});
        LectureResponseDto dto = this.entityToDto(lectures);
        return dto;
    }

    public LectureResponseDto entityToDto(Lectures lectures) {
        LectureResponseDto dto = new LectureResponseDto();
        dto.setDesc(lectures.getLectureContent());
        dto.setLessonCount(lectures.getLectureIndex());
        dto.setTitle(lectures.getLectureName());
        dto.setImageSrc(lectures.getLectureImage());
        dto.setOriginalPrice(lectures.getLecturePrice());
        dto.setId(lectures.getLectureSeq());
        dto.setPaid(true);
        dto.setAuthorName(lectures.getMember().getName());
        dto.setCategory(lectures.getLecturesType().toString());
        return dto;
    }

    @Transactional
    public void saveLecture(LecturesFormDto dto, Member member) {
        //파일 저장
        String saveImageName = fileService.saveFile2(dto.getLectureImage());
        LecturesType type = setLecturesType(dto.getLecturesType());
        Lectures lectures = new Lectures(dto, member, type);
        lectures.setLectureImage(saveImageName);
        Lectures savedLecture = lecturesRepository.save(lectures);
        videoService.saveVideo(savedLecture, dto);
    }

//
//    @Transactional
//    public void updateLecture(long lectureSeq, LectureUpdateDto dto, Member member, MultipartFile file) {
//        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).orElseThrow(() -> new IllegalArgumentException("강의 번호를 확인해주세요"));
//        if (lectures.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
//            try {
//                if (dto.getLecturesType() != null) {
//                    LecturesType type = setLecturesType(dto.getLecturesType());
//                    lectures.setLecturesType(type);
//                }
//                if (dto.getLectureIndex() != null) {
//                    lectures.setLectureIndex(dto.getLectureIndex());
//                }
//                if (dto.getLecturePrice() != null) {
//                    lectures.setLecturePrice(dto.getLecturePrice());
//                }
//                if (dto.getLectureName() != null) {
//                    lectures.setLectureName(dto.getLectureName());
//                }
//                if (dto.getLectureContent() != null) {
//                    lectures.setLectureContent(dto.getLectureContent());
//                }
//                if (!file.isEmpty()) {
//                    String newSaveFilePath = fileService.saveFile2(file);
//                    lectures.setLectureImage(newSaveFilePath);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("강의 업데이트 중 오류가 발생했습니다.");
//            }
//        }
//    }
//
//    public void deleteLecture(long lectureSeq, Member member) {
//        Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).orElseThrow(() -> new IllegalArgumentException("해당 강의가 존재하지 않습니다."));
//        if (lectures.getMember().getUserSeq().equals(member.getUserSeq()) || member.getRole().equals(Role.ADMIN)) {
//            try {
//                lecturesRepository.delete(lectures);
//            } catch (Exception e) {
//                throw new RuntimeException("강의 삭제 도중 오류 발생");
//            }
//        }
//    }
}