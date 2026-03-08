package com.itwillbs.ilkwangtech.standard.service.bom;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BomService {

    void create(BomDTO dto);

    void update(BomDTO dto);
    
    void delete(Long id);

    Page<BomDTO> getList(Pageable pageable);

    Page<BomDTO> getListByItemId(Long itemId, Pageable pageable);
}