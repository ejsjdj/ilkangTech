package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.messenger.entity.MemberFavorite;
import com.itwillbs.ilkwangtech.messenger.entity.MemberFavoriteId;

public interface MemberFavoriteRepository extends JpaRepository<MemberFavorite, MemberFavoriteId> {
	
	List<MemberFavorite> findByMemberId(Long memberId);
	// 특정 사원 간 즐겨찾기 삭제
	void deleteByMemberIdAndTargetId(Long memberId, Long targetId);
    
    // 즐겨찾기 여부 확인
    boolean existsByMemberIdAndTargetId(Long memberId, Long targetId);
}
