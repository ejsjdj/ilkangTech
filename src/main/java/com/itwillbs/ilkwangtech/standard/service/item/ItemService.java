package com.itwillbs.ilkwangtech.standard.service.item;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    void create(ItemDTO dto);

    void update(ItemDTO dto);

    void delete(Long id);

    ItemDTO get(Long id);

    Page<ItemDTO> getList(ItemType type, Pageable pageable);

}
