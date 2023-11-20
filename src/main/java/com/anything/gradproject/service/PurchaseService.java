package com.anything.gradproject.service;

import com.anything.gradproject.constant.SubscribeStatus;
import com.anything.gradproject.dto.*;
import com.anything.gradproject.entity.*;
import com.anything.gradproject.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseListRepository purchaseListRepository;
    private final VideoRepository videoRepository;
    private final SubscribePurchaseRepository subscribePurchaseRepository;

    public PurchaseList savePurchaseList(PurchaseList purchaseList) {
        return purchaseListRepository.save(purchaseList);
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void updateExpiredSubscriptions() {
        List<SubscribePurchase> subscriptions = subscribePurchaseRepository.findAll();

        for (SubscribePurchase subscription : subscriptions) {
            if (subscription.getSubscribeEndDate().isBefore(LocalDateTime.now())) {
                subscription.setSubscribeStatus(SubscribeStatus.EXPIRED);
            }
        }
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

    public String subscribePurchase(Member member, PurchaseDto dto) {
        try {
            List<SubscribePurchase> subscribeList = subscribePurchaseRepository.findByMember_UserSeq(member.getUserSeq());

            // BUY 상태의 구독이 있는지 확인
            boolean hasActiveSubscription = subscribeList.stream()
                    .anyMatch(subP -> subP.getSubscribeStatus().equals(SubscribeStatus.BUY));

            if (hasActiveSubscription) {
                // BUY 상태의 구독이 하나라도 있으면 예외 발생
                throw new IllegalStateException("아직 구독기간이 남았습니다.");
            }

            // BUY 상태의 구독이 없으면 새 구독 저장
            subscribePurchaseRepository.save(SubscribePurchase
                    .builder()
                    .member(member)
                    .dto(dto)
                    .build());
            return "구독권 결제 성공";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public List<SubPurResponseDto> findSubscribe(Member member) {
        List<SubscribePurchase> purchaseList = subscribePurchaseRepository.findByMember_UserSeq(member.getUserSeq());
        return purchaseList.stream().map((pur) -> SubPurResponseDto.builder().purchase(pur).build()).collect(Collectors.toList());
    }

    public AdminPurchaseListDto getPurchaseList() {
        try {
            List<SubscribePurchase> subscribeList = subscribePurchaseRepository.findAll();
            List<SubPurResponseDto> subDtoList = subscribeList.stream().map((sub) -> SubPurResponseDto.builder().purchase(sub).build()).toList();
            List<PurchaseList> purchaseLists = purchaseListRepository.findAll();
            List<LecPurResponseDto> lecPurResponseDtoList = purchaseLists.stream().map((lec) -> LecPurResponseDto.builder().purchaseList(lec).build()).toList();
            return AdminPurchaseListDto.builder().lecPurResponseDtoList(lecPurResponseDtoList).subPurResponseDtoList(subDtoList).build();
        } catch (Exception e) {
            throw new RuntimeException("내역 조회 중 오류");
        }

    }
}