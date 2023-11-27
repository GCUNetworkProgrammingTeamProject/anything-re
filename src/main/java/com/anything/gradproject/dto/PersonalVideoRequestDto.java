package com.anything.gradproject.dto;

import com.anything.gradproject.entity.Member;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PersonalVideoRequestDto {

    private String personalVideoCn; // 개인 영상 링크
    private Member member;
    private MultipartFile file;


}
