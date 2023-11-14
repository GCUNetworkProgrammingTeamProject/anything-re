package com.anything.gradproject.service;

import com.anything.gradproject.dto.InquiryFormDto;
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
public class ShoppingService {

    private final LecturesRepository lecturesRepository;
    private final MemberRepository memberRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final PurchaseListRepository purchaseListRepository;

   public Lectures findLectures(Long lectureSeq){
       Lectures lectures = lecturesRepository.findBylectureSeq(lectureSeq).orElseThrow(() -> {
           return new UsernameNotFoundException("해당 강의를 찾을 수 없습니다.");});

       return lectures;
   }

   public Member findMemberById(){
       Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       UserDetails userDetails = (UserDetails)principal;
       String userid = ((UserDetails) principal).getUsername();

       // 알아낸 id를 통해 계정 번호를 알아냄
       Member member = memberRepository.findById(userid)
               .orElseThrow(() -> {
                   return new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다.");});

       return member;
   }

   public ShoppingList saveShoppingList(ShoppingList shoppingList, Member member){
       String duplicateShopping = checkDuplicateShopping(shoppingList, member);
       if (duplicateShopping != null) {
           throw new IllegalStateException(duplicateShopping);
       }
       String duplicatePurchase = checkDuplicatePurchase(shoppingList, member);
       if (duplicatePurchase != null) {
           throw new IllegalStateException(duplicatePurchase);
       }
       return shoppingListRepository.save(shoppingList);
   }

    public String checkDuplicateShopping(ShoppingList shoppingList, Member member) {
        Optional<ShoppingList> findShoppingList = shoppingListRepository.findByLecturesAndMember(shoppingList.getLectures(), member);
        if (!findShoppingList.isEmpty()) {
            return "이미 담은 강의입니다.";
        }
        return null;
    }

    public String checkDuplicatePurchase(ShoppingList shoppingList, Member member) {
        Optional<PurchaseList> findPurchaseList = purchaseListRepository.findByLecturesAndMember(shoppingList.getLectures(), member);
        if (!findPurchaseList.isEmpty()) {
            return "이미 구매한 강의입니다.";
        }
        return null;
    }

    public List<Lectures> findLecutresByMember(Member member){
       List<ShoppingList> shoppingLists = shoppingListRepository.findByMember(member);
       List<Lectures> lecturesList = new ArrayList<>();

        for (ShoppingList s : shoppingLists ) {
            lecturesList.add(s.getLectures());
        }
        return lecturesList;
    }

    public ShoppingList findDeleteShoppinglist(Long lectureSeq, Member member){
        List<ShoppingList> shoppingList = shoppingListRepository.findByMember(member);

        for (ShoppingList s : shoppingList) {
            if (s.getLectures().getLectureSeq() == lectureSeq){
                return s;
            }
        }
        return null;
    }

    public List<ShoppingList> findDeleteShoppinglists(Member member){
        List<ShoppingList> shoppingList = shoppingListRepository.findByMember(member);

        return shoppingList;
    }
}