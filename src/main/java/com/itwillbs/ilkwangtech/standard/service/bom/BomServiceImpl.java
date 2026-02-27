package com.itwillbs.ilkwangtech.standard.service.bom;

import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.entity.BomEntity;
import com.itwillbs.ilkwangtech.standard.repository.BomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class BomServiceImpl implements  BomService {

    private final BomRepository bomRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(BomDTO dto) {
        bomRepository.save(modelMapper.map(dto, BomEntity.class));
    }

    @Override
    public void update(BomDTO dto) {
        bomRepository.save(modelMapper.map(dto, BomEntity.class));
    }

    @Override
    public void delete(Long id) {
        bomRepository.delete(modelMapper.map(id, BomEntity.class));
    }

    @Override
    public Page<BomDTO> getList(Pageable pageable) {
        return null;
    }

    @Override
    public Page<BomDTO> getListByItemId(Long beforeItemId, Pageable pageable) {
        log.info("🤣👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧");
        log.info("beforeItemId:" + beforeItemId);
        log.info("🤣👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧👨‍🔧");
        Page<BomEntity> entityPage = bomRepository.findByBeforeItem_ItemId(beforeItemId, pageable);
        Page<BomDTO> dtoPage = entityPage.map(
                entity -> modelMapper.map(entity, BomDTO.class)
        );
        return dtoPage;
    }
}
