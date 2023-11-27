package com.anything.gradproject.dto;

import com.anything.gradproject.entity.PerVideoAnalysisDetail;
import com.anything.gradproject.entity.VideoAnalysisDetail;
import lombok.Builder;
import lombok.Data;

@Data
public class AnalysisResponseDto {
    private int timeline;
    private float concentration;
    private long perVideoSeq;

    @Builder
    public AnalysisResponseDto(VideoAnalysisDetail videoAnalysisDetail) {
        this.concentration = videoAnalysisDetail.getConcentration();
        this.timeline = videoAnalysisDetail.getTimeline();
    }

    @Builder(builderMethodName = "personal")
    public AnalysisResponseDto(PerVideoAnalysisDetail perVideoAnalysisDetail) {
        this.concentration = perVideoAnalysisDetail.getConcentration();
        this.timeline = perVideoAnalysisDetail.getTimeline();
        this.perVideoSeq = perVideoAnalysisDetail.getPerVideoAnalysis().getPerVideoAnalysisSeq();
    }

    public static AnalysisResponseDto entityToDto(VideoAnalysisDetail videoAnalysisDetail) {
        return AnalysisResponseDto
                .builder()
                .videoAnalysisDetail(videoAnalysisDetail)
                .build();
    }
    public static AnalysisResponseDto perEntityToDto(PerVideoAnalysisDetail perVideoAnalysisDetail) {
        return AnalysisResponseDto
                .personal()
                .perVideoAnalysisDetail(perVideoAnalysisDetail)
                .build();
    }
}
