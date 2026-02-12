package com.itwillbs.ilkwangtech.standard.service.item;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Override
    public void createItem(ItemDTO itemDTO) {
        itemRepository.save(itemDTO);
    }
}
