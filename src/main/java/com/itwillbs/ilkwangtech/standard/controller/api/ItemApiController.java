package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.item.dto.ItemDTO;
import com.itwillbs.ilkwangtech.standard.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Log4j2
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ItemDTO dto) {
        itemService.create(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.get(id));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody ItemDTO dto) {
        itemService.update(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.ok().build();
    }
}
