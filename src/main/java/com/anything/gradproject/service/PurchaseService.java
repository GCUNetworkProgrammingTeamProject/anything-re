package com.anything.gradproject.service;

import com.anything.gradproject.dto.PurchaseListResponseDto;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseListRepository purchaseListRepository;
    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final LecturesRepository lecturesRepository;

    public PurchaseList savePurchaseList(PurchaseList purchaseList) {
        return purchaseListRepository.save(purchaseList);
    }


    public List<Lectures> findLecutresByMember(Member member) {
        List<PurchaseList> purchaseLists = purchaseListRepository.findByMember(member);
        List<Lectures> lecturesList = new ArrayList<>();

        for (PurchaseList s : purchaseLists) {
            lecturesList.add(s.getLectures());
        }
        return lecturesList;
    }

    public List<Video> findVideoByLectures(long lectureSeq) {
        List<Video> videoList = videoRepository.findByLectures_LectureSeq(lectureSeq);
        return videoList;
    }


}