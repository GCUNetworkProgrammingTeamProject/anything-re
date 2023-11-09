package com.anything.gradproject.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberFormDto {
    private String name;
    private String id;
    private String password;
    private String email;
    private String role;
}