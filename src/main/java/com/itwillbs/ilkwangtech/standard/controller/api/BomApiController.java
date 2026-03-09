package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomTreeDTO;
import com.itwillbs.ilkwangtech.standard.dto.ParentItemDTO;
import com.itwillbs.ilkwangtech.standard.service.bom.BomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

        return ResponseEntity.ok(ApiResponseDTO.success("BOM 목록 조회에 성공했습니다.", result));
    }

    // 다단계 BOM 정전개 조회
    @GetMapping("/tree/{id}")
    public ResponseEntity<ApiResponseDTO<List<BomTreeDTO>>> getBomTree(@PathVariable("id") Long itemId) {
        List<BomTreeDTO> result = bomService.getBomTree(itemId);
        return ResponseEntity.ok(ApiResponseDTO.success("다단계 BOM 조회에 성공했습니다.", result));
    }

    // 다단계 BOM 역전개 조회
    @GetMapping("/where-used/{id}")
    public ResponseEntity<ApiResponseDTO<List<BomTreeDTO>>> getWhereUsedTree(@PathVariable("id") Long itemId) {
        List<BomTreeDTO> result = bomService.getWhereUsedTree(itemId);
        return ResponseEntity.ok(ApiResponseDTO.success("역전개 BOM 조회에 성공했습니다.", result));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'INFORMATION', 'PRODUCTION')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<Void>> create(@RequestBody BomDTO dto) {
        bomService.create(dto);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM이 성공적으로 생성되었습니다."));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'INFORMATION', 'PRODUCTION')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<Void>> update(@RequestBody BomDTO dto) {
        bomService.update(dto);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM 정보가 수정되었습니다."));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'INFORMATION', 'PRODUCTION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable("id") Long bomId) {
        bomService.delete(bomId);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM이 성공적으로 삭제되었습니다."));
    }
}
