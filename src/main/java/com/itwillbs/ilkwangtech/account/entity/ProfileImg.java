package com.itwillbs.ilkwangtech.account.entity;

import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProfileImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgName;
    private String originalImgName;
    private String imgLocation;
    private String repImgYn;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
