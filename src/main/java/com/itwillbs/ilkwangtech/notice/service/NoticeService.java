package com.itwillbs.ilkwangtech.notice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.ilkwangtech.notice.dto.NoticeListDTO;
import com.itwillbs.ilkwangtech.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
	
	private final NoticeRepository noticeRepository;

    public List<NoticeListDTO> getPinnedNotices() {
        return noticeRepository.findTop3ByIsPinnedTrueOrderByRegDateDesc()
                .stream().map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()))
                .collect(Collectors.toList());
    }

    public Page<NoticeListDTO> getNoticeList(int page) {
        // 1-3. 페이지당 10개씩
        Pageable pageable = PageRequest.of(page, 10);
        return noticeRepository.findByIsPinnedFalseOrderByRegDateDesc(pageable)
                .map(n -> new NoticeListDTO(n.getId(), n.getTitle(), n.getWriterName(), 
                        n.getWriterRank(), n.getRegDate(), n.getModDate(), 
                        n.getViewCount(), n.isPinned(), n.isHasAttachment()));
    }

}
