package com.anything.gradproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AnalysisRequestDto {
    private long userSeq;
    private long videoSeq;
    private String recording;

}
