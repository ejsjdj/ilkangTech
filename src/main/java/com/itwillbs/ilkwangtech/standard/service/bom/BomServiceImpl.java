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
    public BomDTO get(Long id) {
        BomEntity entiy = bomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 BOM 이 없습니다."));
        return modelMapper.map(entiy, BomDTO.class);
    }

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
    public Page<BomDTO> getList(String searchField, Pageable pageable) {
        return null;
    }
}
