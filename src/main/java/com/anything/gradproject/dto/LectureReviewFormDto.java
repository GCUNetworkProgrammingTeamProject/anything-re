package com.anything.gradproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LectureReviewFormDto {

    private String revContent;

    private int revScore;

}