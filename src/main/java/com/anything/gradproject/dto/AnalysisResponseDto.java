package com.anything.gradproject.dto;

import com.anything.gradproject.entity.VideoAnalysisDetail;
import lombok.Builder;
import lombok.Data;

@Data
public class AnalysisResponseDto {
    private int timeline;
    private float concentration;

    @Builder
    public AnalysisResponseDto(VideoAnalysisDetail videoAnalysisDetail) {
        this.concentration = videoAnalysisDetail.getConcentration();
        this.timeline = videoAnalysisDetail.getTimeline();
    }

    public static AnalysisResponseDto entityToDto(VideoAnalysisDetail videoAnalysisDetail) {
        return AnalysisResponseDto
                .builder()
                .videoAnalysisDetail(videoAnalysisDetail)
                .build();
    }
}
