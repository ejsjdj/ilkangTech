package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {
    List<ProfileImg> findByMemberIdAndRepImgYn(Long memberId, String repImgYn);
}
