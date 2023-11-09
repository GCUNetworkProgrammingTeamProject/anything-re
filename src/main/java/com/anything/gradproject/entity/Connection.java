package com.anything.gradproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
@Entity
@Table(name = "tb_connction")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Connection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long connectionSeq; // 접속자수 번호 > 날짜는 baseTimeEntity에서 자동으로 찍어줌

    @Column
    private int cntCount; // 접속자 수
}
