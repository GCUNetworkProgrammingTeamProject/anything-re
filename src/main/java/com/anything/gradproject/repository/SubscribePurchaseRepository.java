package com.anything.gradproject.repository;

import com.anything.gradproject.entity.SubscribePurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribePurchaseRepository extends JpaRepository<SubscribePurchase, Long> {

    List<SubscribePurchase> findByMember_UserSeq(long userSeq);
}
