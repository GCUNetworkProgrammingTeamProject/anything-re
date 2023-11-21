package com.anything.gradproject.entity;


import com.anything.gradproject.dto.AdvertiseFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.UUID;


@Entity
@Table(name = "tb_advertise")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Advertisement extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adverSeq; // 광고 번호

    @Column
    private String adverName; // 광고 이름

    @Column
    private long adverClicker; // 광고 조회수

    @Column
    private String adverUrl; // 광고 이동할 url

    @Column
    private String adverImage; // 광고 이미지

    @Column
    private boolean isBanner; // 배너광고 여부



    public static Advertisement createAdvertisement(AdvertiseFormDto advertiseFormDto) {

        Advertisement advertisement = new Advertisement();

        String code = UUID.randomUUID().toString();
        advertisement.setAdverName(advertiseFormDto.getAdverName());
        advertisement.setBanner(false);
        advertisement.setAdverClicker(0);
        advertisement.setAdverUrl(advertiseFormDto.getAdverUrl());
        advertisement.setAdverImage("Advertisement" + code);

        return advertisement;
    }



    public static Advertisement modifyAdvertisement(AdvertiseFormDto advertiseFormDto, Advertisement advertisement) {
        advertisement.setAdverName(advertiseFormDto.getAdverName());
        advertisement.setAdverUrl(advertiseFormDto.getAdverUrl());
        advertisement.setAdverClicker(0);

        return advertisement;
    }

}
