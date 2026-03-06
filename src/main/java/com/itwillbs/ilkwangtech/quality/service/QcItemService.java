package com.itwillbs.ilkwangtech.quality.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.quality.entity.QcItem;
import com.itwillbs.ilkwangtech.quality.repository.QcItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QcItemService {

	private final QcItemRepository qcItemRepository;
	
	public List<QcItem> findAllQcItems() {
        return qcItemRepository.findAll();
    }
}
