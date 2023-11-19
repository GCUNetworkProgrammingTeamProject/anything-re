package com.anything.gradproject.entity;


import com.anything.gradproject.dto.InquiryFormDto;
import com.anything.gradproject.repository.MemberRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_inquiry")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Inquiry extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long inquirySeq; // 문의 번호

    @Column
    private String inquiryTitle; // 질문 제목

    @Column
    private String inquiryQuestion; // 질문 내용

    @Column
    private boolean inquiryNotice; // 공지사항

    @Column
    private boolean inquiryIsSecret; // 타인에게 공개 유무

    @Column
    private boolean inquiryIsAnswered; // 답변 유무

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 객체가 삭제되면 같이 삭제됨
    @JoinColumn(name = "user_seq")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "Video_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Video video;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"inquiry", "member", "video"})
    private List<InquiryAnswer> inquiryAnswerList;


    public static Inquiry createInquiry(InquiryFormDto inquiryFormDto, Member member, Video video) {

        Inquiry inquiry = new Inquiry();

        inquiry.setMember(member);
        inquiry.setVideo(video);
        inquiry.setInquiryTitle(inquiryFormDto.getInquiryTitle());
        inquiry.setInquiryQuestion(inquiryFormDto.getInquiryQuestion());
        inquiry.setInquiryNotice(false);

        if(inquiryFormDto.getInquiryIsSecret().equals("true"))
            inquiry.setInquiryIsSecret(true);
        else {
            inquiry.setInquiryIsSecret(false);
        }
        inquiry.setInquiryIsAnswered(false);

        return inquiry;
    }

    public static Inquiry modifyInquiry(InquiryFormDto inquiryFormDto, Inquiry inquiry) {

        inquiry.setInquiryTitle(inquiryFormDto.getInquiryTitle());
        inquiry.setInquiryQuestion(inquiryFormDto.getInquiryQuestion());
        inquiry.setInquiryIsSecret(false);
        if(inquiryFormDto.getInquiryIsSecret().equals("true"))
            inquiry.setInquiryIsSecret(true);
        else
            inquiry.setInquiryIsSecret(false);

        return inquiry;
    }

}
