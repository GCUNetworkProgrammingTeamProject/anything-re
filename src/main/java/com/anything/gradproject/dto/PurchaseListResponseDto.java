package com.anything.gradproject.dto;

import com.anything.gradproject.constant.PurchaseStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseListResponseDto {
    private String lectureName;
    private String TeacherName;
    private int lecturePrice;
    private LocalDateTime purchaseDate;
    private PurchaseStatus purchaseStatus;
}
