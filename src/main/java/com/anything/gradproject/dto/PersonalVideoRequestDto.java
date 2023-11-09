package com.anything.gradproject.dto;

import com.anything.gradproject.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class PersonalVideoRequestDto {

//    private long personalVideoSeq; // 개인 영상 번호
    private String personalVideoCn; // 개인 영상 링크
    private Member member;


}
