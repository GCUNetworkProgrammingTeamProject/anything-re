package com.anything.gradproject.entity;

import com.anything.gradproject.constant.SubscribeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.Date;

public class SubscribePurchase extends BaseEntity{

    @Id
    private long subscribeSeq; // 구독권 결제번호

    @Column
    private Date subscribeEndDate; // 만료일자

    @Column
    private String subscribePur; // 구매방법

    @Column
    private int subscribePrice; //가격

    @Column
    private SubscribeStatus subscribeStatus; // 주문 상태

}
