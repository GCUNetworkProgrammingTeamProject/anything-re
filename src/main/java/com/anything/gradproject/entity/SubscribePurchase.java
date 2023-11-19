package com.anything.gradproject.entity;

import com.anything.gradproject.constant.SubscribeStatus;
import com.anything.gradproject.dto.PurchaseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_subscribe_purchase")
public class SubscribePurchase extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subscribeSeq; // 구독권 결제번호
    private LocalDateTime subscribeEndDate; // 만료일자
    private Integer subscribePrice; //가격
    @Enumerated(EnumType.STRING)
    private SubscribeStatus subscribeStatus; // 주문 상태
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member;

    @Builder
    public SubscribePurchase(PurchaseDto dto, Member member) {
        this.member = member;
        this.subscribePrice = dto.getPrice();
        this.subscribeEndDate = LocalDateTime.now().plusMonths(1);
        this.subscribeStatus = SubscribeStatus.BUY;
    }

    public void setSubscribeStatus(SubscribeStatus subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
}
