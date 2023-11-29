package com.anything.gradproject.repository;

import com.anything.gradproject.entity.Advertisement;
import com.anything.gradproject.entity.Lectures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Advertisement findByadverSeq(long adverSeq);
    List<Advertisement> findByIsBanner(boolean bol);
}
