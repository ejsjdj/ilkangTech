package com.itwillbs.ilkwangtech.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.ilkwangtech.quality.entity.QcItem;

public interface QcItemRepository extends JpaRepository<QcItem, Long> {

}
