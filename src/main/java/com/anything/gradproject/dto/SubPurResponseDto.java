package com.anything.gradproject.dto;

import com.anything.gradproject.entity.SubscribePurchase;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class SubPurResponseDto {

    private String status;
    private String startDate;
    private String endDate;

    @Builder
    public SubPurResponseDto(SubscribePurchase purchase) {
        this.endDate = purchase.getSubscribeEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        this.startDate = purchase.getRegTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        this.status = purchase.getSubscribeStatus().toString();
    }
}
