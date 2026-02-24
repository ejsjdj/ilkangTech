package com.itwillbs.ilkwangtech.standard.controller.api;

import com.itwillbs.ilkwangtech.item.dto.BomDTO;
import groovy.util.logging.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bom")
@Log4j2
public class BomApiController {

    @GetMapping("/list")
    public ResponseEntity<BomDTO> getBomList() {

        return ResponseEntity.ok(new BomDTO());

    }

}
