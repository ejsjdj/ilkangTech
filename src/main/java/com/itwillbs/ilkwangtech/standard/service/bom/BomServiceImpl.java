package com.itwillbs.ilkwangtech.standard.service.bom;

import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.dto.ParentItemDTO;
import com.itwillbs.ilkwangtech.standard.entity.BomEntity;
import com.itwillbs.ilkwangtech.standard.entity.ItemEntity;
import com.itwillbs.ilkwangtech.standard.mapper.BomMapper;
import com.itwillbs.ilkwangtech.standard.repository.BomRepository;
import com.itwillbs.ilkwangtech.standard.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BomServiceImpl implements  BomService {

    private final BomRepository bomRepository;
    private final ItemRepository itemRepository;
    private final BomMapper bomMapper;
    private final ModelMapper modelMapper;

    @Override
    public void create(BomDTO dto) {
        // 1. DTO의 ID로 ItemEntity 조회
        ItemEntity beforeItem = itemRepository.findById(dto.getParentItemId()).orElse(null);
        ItemEntity afterItem = itemRepository.findById(dto.getChildItemId()).orElse(null);

        // 2. BomEntity 조립, 필요수량 입력
        BomEntity bom = new BomEntity();
        bom.setParentItem(beforeItem);
        bom.setChildItem(afterItem);
        bom.setRequireQty(dto.getRequiredQty());

        // 3. DB 저장
        bomRepository.save(bom);
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

    /**
     * 하위 아이템의 id 를 이용해서
     * 상위 단계의 아이템들의 이름을 가져오는
     * 메서드
     * @param childItemId
     * @param pageable
     * @return
     */
    @Override
    public List<ParentItemDTO> getListByItemId(Long childItemId, Pageable pageable) {

        List<ParentItemDTO> result = bomMapper.selectBomList(childItemId, pageable);

        return result;
    }
}
