package com.itwillbs.ilkwangtech.standard.repository;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final ItemMapper itemMapper;

    public void save(ItemDTO itemDTO) {

        itemMapper.insertItem(itemDTO);

    }
}
