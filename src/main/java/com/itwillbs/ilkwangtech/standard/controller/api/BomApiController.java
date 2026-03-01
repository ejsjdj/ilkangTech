package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.dto.ParentItemDTO;
import com.itwillbs.ilkwangtech.standard.service.bom.BomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bom")
@RequiredArgsConstructor
@Log4j2
public class BomApiController {

    private final BomService bomService;

    // 부품의 BOM 목록 조회
    @GetMapping("/child/{id}")
    public ResponseEntity<ApiResponseDTO<List<ParentItemDTO>>> getList(
            @PathVariable("id") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sortBy", defaultValue = "bomId") String sortBy,
            @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction) {


        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));
        List<ParentItemDTO> result = bomService.getListByItemId(itemId, pageable);

        for (ParentItemDTO dto : result) {
            log.info("dto : {}", dto);
        }

        log.info("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁");
        log.info("resultSize() : {}", result.size());
        log.info("childItemId() : {}", itemId);
        log.info("😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁😁");

        return ResponseEntity.ok(ApiResponseDTO.success("BOM 목록 조회에 성공했습니다.", result));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<Void>> create(@RequestBody BomDTO dto) {

        bomService.create(dto);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM이 성공적으로 생성되었습니다."));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<Void>> update(@RequestBody BomDTO dto) {
        bomService.update(dto);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM 정보가 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable("beforeItemId") Long bomId) {
        bomService.delete(bomId);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM이 성공적으로 삭제되었습니다."));
    }
}
