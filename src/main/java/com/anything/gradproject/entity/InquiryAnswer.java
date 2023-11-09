package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table(name = "tb_inquiry_answer")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InquiryAnswer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long inquiryAnswerSeq; // 답변 번호

    @Column
    private String inquiryAnswer; // 질문에 대한 답변

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "inquiry_seq")
    @OnDelete(action = OnDeleteAction.CASCADE) // 연관된 inquiry가 삭제되면 같이 삭제됨
    private Inquiry inquiry;



    public static InquiryAnswer createInquiryAnswer(String answerString, Member member, Inquiry inquiry){
        InquiryAnswer inquiryAnswer = new InquiryAnswer();

        inquiryAnswer.setInquiryAnswer(answerString);
        inquiryAnswer.setMember(member);
        inquiryAnswer.setInquiry(inquiry);

        return inquiryAnswer;

    }

    public static InquiryAnswer updateInquiryAnswer(InquiryAnswer inquiryAnswer, String answerString, long inquiryAnswerSeq){

        inquiryAnswer.setInquiryAnswerSeq(inquiryAnswerSeq);
        inquiryAnswer.setInquiryAnswer(answerString);

        return inquiryAnswer;
    }
}
