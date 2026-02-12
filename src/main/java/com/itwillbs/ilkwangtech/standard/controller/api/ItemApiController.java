package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.sales.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.standard.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDTO<List<ItemDTO>>> createItem(@RequestBody ItemDTO itemDTO) {

        itemService.createItem(itemDTO);

        return null;
    }
}
