package com.itwillbs.ilkwangtech.standard.service.item;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    void create(ItemDTO dto);

    void create(ItemDTO dto, List<MultipartFile> itemImgFileList) throws Exception;

    void update(ItemDTO dto);

    void update(ItemDTO dto, List<MultipartFile> itemImgFileList) throws Exception;

    void delete(Long id);

    ItemDTO get(Long id);

    Page<ItemDTO> getList(ItemType type, Pageable pageable);

    Page<ItemDTO> getBomList(ItemType type, Pageable pageable);

}
