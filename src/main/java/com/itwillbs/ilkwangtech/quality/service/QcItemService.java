package com.itwillbs.ilkwangtech.quality.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.quality.dto.DisposalHistoryDto;
import com.itwillbs.ilkwangtech.quality.dto.QcHistoryDto;
import com.itwillbs.ilkwangtech.quality.entity.QcItem;
import com.itwillbs.ilkwangtech.quality.entity.QcRejectReason;
import com.itwillbs.ilkwangtech.quality.repository.QcItemRepository;
import com.itwillbs.ilkwangtech.quality.repository.QcRejectReasonRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QcItemService {

	private final QcItemRepository qcItemRepository;
	private final QcRejectReasonRepository qcRejectReasonRepository;
	
	public List<QcItem> findAllQcItems() {
        return qcItemRepository.findAll();
    }

	public QcItem saveQcItem(QcItem qcItem) {
		return qcItemRepository.save(qcItem);
	}
	
	public List<QcHistoryDto> getQcHistoryList() {
        return qcItemRepository.findQcHistoryList();
    }
	
	@Transactional
    public void saveRejectReason(Long workerId, String reasonText) {
        // 기존 사유가 있으면 가져오고, 없으면 새로 객체 생성
        QcRejectReason reason = qcRejectReasonRepository.findByWorkerId(workerId)
                .orElse(QcRejectReason.builder().workerId(workerId).build());
        
        // 내용 업데이트 및 저장
        reason.setRejectReason(reasonText);
        qcRejectReasonRepository.save(reason);
    }
	
	@Transactional
	public void saveDisposalHistory(Long workerId) {
	    qcItemRepository.insertDisposalHistory(workerId);
	}
	
	public List<DisposalHistoryDto> getDisposalList() {
        return qcItemRepository.findDisposalList();
    }
}
