package com.itwillbs.ilkwangtech.standard.service.bom;

import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BomService {

    // id 로 해당 bom 을 찾는 기능이 필요한가??
    BomDTO get(Long id);

    void create(BomDTO dto);

    void update(BomDTO dto);
    
    void delete(Long id);

    Page<BomDTO> getList(Pageable pageable);

    Page<BomDTO> getList(String searchField, Pageable pageable);

}