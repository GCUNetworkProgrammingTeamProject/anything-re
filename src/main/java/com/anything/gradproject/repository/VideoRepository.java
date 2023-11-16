package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByVideoSeqAndLectures(long videoSeq, Lectures lecture);
    List<Video> findByLectures(Lectures lecture);

    List<Video> findByLectures_LectureSeq(long lectureSeq);
    Optional<Video> findByVideoSeq(long videoSeq);


}
