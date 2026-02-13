package com.itwillbs.ilkwangtech.standard.mapper;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper {

    void insertItem(ItemDTO itemDTO);

}
