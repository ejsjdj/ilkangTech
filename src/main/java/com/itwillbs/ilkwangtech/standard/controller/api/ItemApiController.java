package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.item.constant.ItemType;
import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
@Log4j2
public class ItemApiController {

    private final ItemService itemService;

    // 완제품, 재공품, 원자재를 필터로 해서 pageable 을 실시한다.
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<Page<ItemDTO>>> getList(
            @RequestParam(name = "type", defaultValue = "") ItemType type,
            @PageableDefault(page = 0, size = 10, sort = "itemId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemDTO> result = itemService.getList(type, pageable);

        return ResponseEntity.ok(ApiResponseDTO.success("품목 목록 조회에 성공했습니다.", result));
    }

    @GetMapping("/bomList")
    public ResponseEntity<ApiResponseDTO<Page<ItemDTO>>> getBomList(
            @RequestParam(name = "type", defaultValue = "") ItemType type,
            @PageableDefault(page = 0, size = 10, sort = "itemId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemDTO> result = itemService.getBomList(type, pageable);

        return ResponseEntity.ok(ApiResponseDTO.success("품목 목록 조회에 성공했습니다.", result));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'PRODUCTION')")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponseDTO<Void>> create(
            @RequestPart("itemDTO") ItemDTO dto,
            @RequestPart(value = "itemImgFile", required = false) List<MultipartFile> itemImgFileList) throws Exception {
        
        if(itemImgFileList == null || itemImgFileList.isEmpty()) {
            itemService.create(dto);
        } else {
            itemService.create(dto, itemImgFileList);
        }
        return ResponseEntity.ok(ApiResponseDTO.success("품목 등록에 성공했습니다."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ItemDTO>> get(@PathVariable Long id) {
        ItemDTO item = itemService.get(id);
        return ResponseEntity.ok(ApiResponseDTO.success("품목 조회에 성공했습니다.", item));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'PRODUCTION')")
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponseDTO<Void>> update(
            @PathVariable Long id,
            @RequestPart("itemDTO") ItemDTO dto,
            @RequestPart(value = "itemImgFile", required = false) List<MultipartFile> itemImgFileList) throws Exception {
        
        dto.setItemId(id);
        if(itemImgFileList == null || itemImgFileList.isEmpty()) {
            itemService.update(dto);
        } else {
            itemService.update(dto, itemImgFileList);
        }
        return ResponseEntity.ok(ApiResponseDTO.success("품목 수정에 성공했습니다."));
    }

    @PreAuthorize("hasAnyAuthority('CEO', 'PRODUCTION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("품목 삭제에 성공했습니다."));
    }

}
