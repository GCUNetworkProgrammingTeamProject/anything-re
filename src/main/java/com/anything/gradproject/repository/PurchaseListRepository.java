package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.PurchaseList;
import com.anything.gradproject.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseListRepository extends JpaRepository<PurchaseList, Long> {
    List<PurchaseList> findByMember(Member member);

    Optional<PurchaseList> findByLectures(Lectures lectures);

    List<PurchaseList> findByMemberAndLectures(Member member, Lectures lectures);

    Optional<PurchaseList> findByLecturesAndMember(Lectures lectures, Member member);
}
