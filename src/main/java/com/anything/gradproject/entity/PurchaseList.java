package com.anything.gradproject.entity;

import com.anything.gradproject.constant.PurchaseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Table(name = "tb_purchase_list")
@ToString
@NoArgsConstructor
public class PurchaseList extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purSeq; // 주문 번호

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus; // 주문 상태

    @ManyToOne
    @JoinColumn(name = "user_seq")

    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lectures lectures;


    public PurchaseList createPurchaseList(ShoppingList s){
        PurchaseList purchaseList = new PurchaseList();

        purchaseList.setMember(s.getMember());
        purchaseList.setLectures(s.getLectures());
        purchaseList.setPurchaseStatus(PurchaseStatus.BUY);

        return purchaseList;
    }
}
