package com.itwillbs.ilkwangtech.standard.mapper;

import com.itwillbs.ilkwangtech.standard.dto.ParentItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface BomMapper {

    List<ParentItemDTO> selectBomList(@Param("childItemId") Long childItemId, @Param("pageable") Pageable pageable);

//    Long countBomList(@Param("childItemId") Long childItemId);

}