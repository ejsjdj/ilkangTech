package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    // 아이템 타입으로 리스트 검색해서 페이징처리
    // itemType 을 Enum 으로 받아서 검색 필터링이 되는가??
    Page<ItemEntity> findByItemType(ItemType itemType, Pageable pageable);

}
