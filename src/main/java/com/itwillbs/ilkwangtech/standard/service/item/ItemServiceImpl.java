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
        ItemEntity item = modelMapper.map(dto, ItemEntity.class);
        itemRepository.save(item);
    }

    @Override
    public void update(ItemDTO dto) {
        ItemEntity item = modelMapper.map(dto, ItemEntity.class);
        itemRepository.save(item);
    }

    @Override
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDTO get(Long id) {
        ItemDTO item = modelMapper.map(itemRepository.findByItemId(id), ItemDTO.class);
        return item;
    }

    @Override
    public Page<ItemDTO> getList(ItemType itemType, Pageable pageable) {
        Page<ItemEntity> entityPage = itemRepository.findByItemType(itemType, pageable);
        return entityPage.map(entity -> modelMapper.map(entity, ItemDTO.class));
    }

    @Override
    public Page<ItemDTO> getBomList(ItemType itemType, Pageable pageable) {
        Page<ItemEntity> entityPage;
        if (itemType.getCode() == 2) {
            itemType = ItemType.RAW;
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            log.info("itemType: " + itemType);
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            entityPage = itemRepository.findByItemType(itemType, pageable);
        } else if (itemType.getCode() == 3) {
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            log.info("FG");
            log.info("itemType: " + itemType);
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            entityPage = itemRepository.findByItemTypeNot(itemType, pageable);
        } else {
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            log.info("RAW");
            log.info("itemType: " + itemType);
            log.info("😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂");
            entityPage = itemRepository.findByItemType(itemType, pageable);
        }
        return entityPage.map(entity -> modelMapper.map(entity, ItemDTO.class));
    }

}