package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Page<ItemEntity> findByItemType(ItemType itemType, Pageable pageable);

    ItemEntity findByItemId(Long id);
    
    @Query("SELECT DISTINCT i FROM ItemEntity i")
    List<ItemEntity> findAllDistinct();

    Page<ItemEntity> findByItemTypeNot(ItemType itemType, Pageable pageable);


}
