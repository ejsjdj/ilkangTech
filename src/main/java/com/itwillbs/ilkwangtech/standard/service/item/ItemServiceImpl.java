package com.itwillbs.ilkwangtech.standard.service.item;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(ItemDTO dto) {

    }

    @Override
    public void update(ItemDTO dto) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ItemDTO get(Long id) {
        return null;
    }

    @Override
    public Page<ItemDTO> getList(ItemType itemType, Pageable pageable) {
        Page<ItemEntity> entityPage = itemRepository.findByItemType(itemType, pageable);
        return entityPage.map(entity -> modelMapper.map(entity, ItemDTO.class));
    }

}