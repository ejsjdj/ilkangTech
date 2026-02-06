package com.itwillbs.ilkwangtech.account.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor // @Builder를 사용하려면 모든 필드 생성자가 필요합니다.
@NoArgsConstructor
public class ProfileImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgName;
    private String originalImgName;
    private String imgLocation;
    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void unmarkRepresentative() {
        this.repImgYn = "N";
    }

    public static ProfileImg of(String imgName, String originalImgName, String imgLocation, Member member) {
        return ProfileImg.builder()
                .imgName(imgName)
                .originalImgName(originalImgName)
                .imgLocation(imgLocation)
                .repImgYn("Y")
                .member(member)
                .build();
    }
}