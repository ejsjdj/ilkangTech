package com.itwillbs.ilkwangtech.sales.mapper;

import com.itwillbs.ilkwangtech.sales.constant.CompanyCategory;
import com.itwillbs.ilkwangtech.sales.dto.CompanyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CompanyMapper {

    void insertCompany(CompanyDTO companyDTO);

    CompanyDTO selectByName(String name);

    List<CompanyDTO> selectByPage(@Param("offset") long offset,
                                  @Param("pageSize") long pageSize,
                                  @Param("companyType") CompanyCategory companyType);

    long selectCount(@Param("companyType") CompanyCategory companyType);

}