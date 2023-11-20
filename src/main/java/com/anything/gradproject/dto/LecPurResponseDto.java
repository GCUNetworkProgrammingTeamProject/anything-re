package com.anything.gradproject.dto;

import com.anything.gradproject.constant.PurchaseStatus;
import com.anything.gradproject.entity.Lectures;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.PurchaseList;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class LecPurResponseDto {


    private long purSeq; // 주문 번호

    private String purchaseStatus; // 주문 상태
    private String purchaseDate;

    private MemberResponseDto member;

    private LectureResponseDto lecture;

    @Builder
    public LecPurResponseDto(PurchaseList purchaseList) {
        this.purSeq = purchaseList.getPurSeq();
        this.purchaseStatus = purchaseList.getPurchaseStatus().toString();
        this.member = MemberResponseDto.builder().member(purchaseList.getMember()).build();
        this.lecture = LectureResponseDto.builder().lectures(purchaseList.getLectures()).build();
        this.purchaseDate = purchaseList.getRegTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }
}
