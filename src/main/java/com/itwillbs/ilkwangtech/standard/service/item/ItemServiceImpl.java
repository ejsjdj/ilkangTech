package com.itwillbs.ilkwangtech.standard.service.item;

import com.itwillbs.ilkwangtech.common.annotation.Audit;
import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.item.dto.ItemImgDTO;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemImg;
import com.itwillbs.ilkwangtech.standard.repository.ItemImgRepository;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ModelMapper modelMapper;

    @Value("${file.uploadBaseLocation:/upload/}")
    private String uploadBaseLocation;

    private final String itemImgPath = "item/";

    @Override
    @Audit(action = "품목 등록", entity = "Item")
    public void create(ItemDTO dto) {
        ItemEntity item = modelMapper.map(dto, ItemEntity.class);
        itemRepository.save(item);
    }

    @Override
    @Audit(action = "품목 등록(이미지 포함)", entity = "Item")
    public void create(ItemDTO dto, List<MultipartFile> itemImgFileList) throws Exception {
        ItemEntity item = modelMapper.map(dto, ItemEntity.class);
        itemRepository.save(item);

        for(int i=0; i<itemImgFileList.size(); i++) {
            MultipartFile imgFile = itemImgFileList.get(i);
            if(!imgFile.isEmpty()) {
                String repImgYn = (i == 0) ? "Y" : "N";
                saveItemImg(item, imgFile, repImgYn);
            }
        }
    }

    private void saveItemImg(ItemEntity item, MultipartFile imgFile, String repImgYn) throws Exception {
        String oriImgName = imgFile.getOriginalFilename();
        String imgName = UUID.randomUUID().toString() + "_" + oriImgName;
        String fullPath = uploadBaseLocation + itemImgPath;
        
        File directory = new File(fullPath);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        imgFile.transferTo(new File(fullPath + imgName));

        ItemImg itemImg = ItemImg.of(imgName, oriImgName, itemImgPath, item, repImgYn);
        itemImgRepository.save(itemImg);
    }

    @Override
    @Audit(action = "품목 수정", entity = "Item")
    public void update(ItemDTO dto) {
        ItemEntity item = modelMapper.map(dto, ItemEntity.class);
        itemRepository.save(item);
    }

    @Override
    @Audit(action = "품목 수정(이미지 포함)", entity = "Item")
    public void update(ItemDTO dto, List<MultipartFile> itemImgFileList) throws Exception {
        ItemEntity item = itemRepository.findById(dto.getItemId()).orElseThrow();
        modelMapper.map(dto, item);
        itemRepository.save(item);

        if(itemImgFileList != null && !itemImgFileList.isEmpty()) {
            // 기존 이미지 삭제 (단순화를 위해 일단 새로 추가)
            // 실제 구현에서는 기존 이미지를 관리하는 로직이 더 복잡할 수 있음
            for(MultipartFile imgFile : itemImgFileList) {
                if(!imgFile.isEmpty()) {
                    saveItemImg(item, imgFile, "N");
                }
            }
        }
    }

    @Override
    @Audit(action = "품목 삭제", entity = "Item")
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDTO get(Long id) {
        ItemEntity entity = itemRepository.findByItemId(id);
        ItemDTO dto = modelMapper.map(entity, ItemDTO.class);
        
        List<ItemImg> itemImgs = itemImgRepository.findByItemItemIdOrderByIdAsc(id);
        List<ItemImgDTO> imgDtos = itemImgs.stream()
                .map(img -> ItemImgDTO.builder()
                        .id(img.getId())
                        .imgName(img.getImgName())
                        .originalImgName(img.getOriginalImgName())
                        .imgUrl("/display?fileName=" + img.getImgLocation() + img.getImgName())
                        .repImgYn(img.getRepImgYn())
                        .build())
                .collect(Collectors.toList());
        dto.setItemImgList(imgDtos);
        
        return dto;
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