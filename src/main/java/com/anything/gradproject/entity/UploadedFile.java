package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String filename;

    @ManyToOne
    @JoinColumn(name = "cp_seq")
    private CommunityPost communityPost;

}
