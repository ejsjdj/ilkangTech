package com.itwillbs.ilkwangtech.standard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_img")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 서버에 저장된 파일명
    private String originalImgName; // 원본 파일명
    private String imgLocation; // 파일 저장 경로
    private String repImgYn; // 대표 이미지 여부 ('Y', 'N')

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    public void unmarkRepresentative() {
        this.repImgYn = "N";
    }

    public void updateRepImgYn(String repImgYn) {
        this.repImgYn = repImgYn;
    }

    public static ItemImg of(String imgName, String originalImgName, String imgLocation, ItemEntity item, String repImgYn) {
        return ItemImg.builder()
                .imgName(imgName)
                .originalImgName(originalImgName)
                .imgLocation(imgLocation)
                .repImgYn(repImgYn)
                .item(item)
                .build();
    }
}
