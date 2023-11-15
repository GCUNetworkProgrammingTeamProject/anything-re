package com.anything.gradproject.entity;

import com.anything.gradproject.constant.Role;
import com.anything.gradproject.constant.TeacherStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.ErrorResponse;

import java.util.List;

@Entity
@Table(name = "tb_community_post")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CommunityPost extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long cpSeq;

    @Column(nullable = false)
    private String cpTitle;

    @Column(nullable = false)
    @Lob
    private String cpContent;

    @Column
    private boolean cpType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_seq")
    private Member member;

    @OneToMany(mappedBy = "communityPost", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"communityPost", "member"})
    private List<CommunityComment> communityComment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "communityPost")
    private List<UploadedFile> uploadedFiles;


    @Builder
    public CommunityPost(String title, String content, Member member) {
        this.cpTitle = title;
        this.cpContent = content;
        this.member = member;
        this.cpType = false;
    }

    public void update(String cpTitle, String cpContent, Member member) {
        this.cpTitle = cpTitle;
        this.cpContent = cpContent;
        this.member = member;
    }

}
