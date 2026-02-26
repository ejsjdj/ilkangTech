package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.standard.dto.BomDTO;
import com.itwillbs.ilkwangtech.standard.service.bom.BomService;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bom")
@RequiredArgsConstructor
@Log4j2
public class BomApiController {

    private final BomService bomService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<Page<BomDTO>>> getList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "searchField", defaultValue = "") String searchField,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));
        Page<BomDTO> result;

        if (searchField != null && !searchField.isEmpty()) result = bomService.getList(searchField, pageable);
        else result = bomService.getList(pageable);

        return ResponseEntity.ok(ApiResponseDTO.success("BOM 목록 조회에 성공했습니다.", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<BomDTO>> get(@PathVariable Long id) {
        BomDTO bom = bomService.get(id);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM 상세 조회에 성공했습니다.", bom));
    }

    // BOM 을 만들때
    // 부모아이템과 자식아이템의 id 값을 받아와서
    // 각 dto 에 집어넣고 insert
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
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        bomService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("BOM이 성공적으로 삭제되었습니다."));
    }
}
