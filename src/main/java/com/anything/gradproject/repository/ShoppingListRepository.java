package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findByMember(Member member);
    Optional<ShoppingList> findByLectures(Lectures lectures);
Optional<ShoppingList> findByLecturesAndMember(Lectures lectures, Member member);
}
