package com.itwillbs.ilkwangtech.account.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "profile_img")
@SequenceGenerator(name = "profile_img_seq", sequenceName = "profile_img_seq", initialValue = 1000, allocationSize = 1)
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_img_seq")
    private Long profileImgId;
    private String imgName;
    private String originalImgName;
    private String imgLocation;
    private String repImgYn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;
}
