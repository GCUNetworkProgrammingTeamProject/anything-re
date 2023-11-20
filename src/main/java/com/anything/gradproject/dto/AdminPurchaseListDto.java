package com.anything.gradproject.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AdminPurchaseListDto {
    private List<SubPurResponseDto> subscribe;
    private List<LecPurResponseDto> lectures;

    @Builder
    public AdminPurchaseListDto(List<SubPurResponseDto> subPurResponseDtoList, List<LecPurResponseDto> lecPurResponseDtoList) {
        this.lectures = lecPurResponseDtoList;
        this.subscribe = subPurResponseDtoList;
    }
}
