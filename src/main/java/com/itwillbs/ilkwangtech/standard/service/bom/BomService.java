package com.itwillbs.ilkwangtech.standard.service.bom;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomTreeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ParentItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BomService {

    void create(BomDTO dto);

    void update(BomDTO dto);
    
    void delete(Long id);

    Page<BomDTO> getList(Pageable pageable);

    List<ParentItemDTO> getListByItemId(Long itemId, Pageable pageable);

    List<BomTreeDTO> getBomTree(Long itemId);

    List<BomTreeDTO> getWhereUsedTree(Long itemId);
}