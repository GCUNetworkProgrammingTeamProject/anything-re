package com.anything.gradproject.dto;

import lombok.Data;

@Data
public class TeacherDenyDto {
    private String reason;
    private long teacherDetailSeq;
    private long status;
}
