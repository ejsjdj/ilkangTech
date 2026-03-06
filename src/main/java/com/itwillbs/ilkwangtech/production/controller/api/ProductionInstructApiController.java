package com.itwillbs.ilkwangtech.production.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructInsertDTO;
import com.itwillbs.ilkwangtech.production.service.ProductionDefectiveService;
import com.itwillbs.ilkwangtech.production.service.ProductionInstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// 작업지시 컨트롤러
@RestController
@RequestMapping("/api/production_instruct")
@RequiredArgsConstructor
public class ProductionInstructApiController {

    private final ProductionInstructService productionInstructService;
    private final ProductionDefectiveService productionDefectiveService;


    @GetMapping("/list")
    public Page<ProductionInstructDTO> getProductionInstructList(Pageable pageable,
                                                                 @RequestParam(value = "keyword") String keyword){

        Page<ProductionInstructDTO> list = productionInstructService.getProductionInstructList(pageable, keyword);

        return list;
    }

    // 2. 작업지시 상세
    @GetMapping("/detail")
    public Optional<ProductionInstructDetailDTO> getProductionInstructDetail(@RequestParam(value = "instructId") Long instructId){

        Optional<ProductionInstructDetailDTO> detail = productionInstructService.getProductionInstructDetail(instructId);

        return detail;

    }

    // 3. 작업지시 등록
    @PostMapping("/register")
    public  ResponseEntity<?> insertProductionInstruct(@RequestBody ProductionInstructInsertDTO productionInstructInsertDTO,
                                                        @AuthenticationPrincipal AccountLogin accountLogin){
        System.out.println("작업지시 등록 컨트롤러 실행됨!!!!!!");

        Long userId = accountLogin.getId();

        productionInstructService.saveProductionInstruct(productionInstructInsertDTO, userId);

        return ResponseEntity.ok().build();
    }



    // 4. 불량 수량 등록
    public void updateProductionInstructDefective(@RequestParam(value = "DefectiveQty") Long defectiveQty,
                                                  @RequestParam(value = "instructId") Long instructId,
                                                  @RequestParam(value = "workerId") Long workerId){

        productionDefectiveService.instructDefective(defectiveQty, instructId, workerId);

    }

    // 5. 작업시지 공정 생산시작
    @PostMapping("/start")
    public void startProductionInstruct(@RequestParam(value = "planeId") Long planeId,
                                        @RequestParam(value = "instructId") Long instructId,
                                        @RequestParam(value = "workerId") Long workerId){

        productionInstructService.startInstruct(planeId, instructId, workerId);
    }

    // 6. 작업지시 공정 완료
    @PostMapping("/complete")
    public void completeProductionInstruct(@RequestParam(value = "planeId") Long planeId,
                                        @RequestParam(value = "instructId") Long instructId,
                                        @RequestParam(value = "workerId") Long workerId){

        productionInstructService.completeInstruct(planeId, instructId, workerId);
    }

    // 7. 작업지시 공정 중단
    @PostMapping("/cancel")
    public void cancelProductionInstruct(@RequestParam(value = "planeId") Long planeId,
                                        @RequestParam(value = "instructId") Long instructId,
                                        @RequestParam(value = "workerId") Long workerId){

        productionInstructService.cancelInstruct(planeId, instructId, workerId);
    }
}