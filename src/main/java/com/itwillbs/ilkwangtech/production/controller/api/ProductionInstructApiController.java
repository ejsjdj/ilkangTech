package com.itwillbs.ilkwangtech.production.controller.api;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructDetailDTO;
import com.itwillbs.ilkwangtech.production.dto.ProductionInstructInsertDTO;
import com.itwillbs.ilkwangtech.production.service.ProductionInstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

// 작업지시 컨트롤러
@Controller
@RequiredArgsConstructor
public class ProductionInstructApiController {

    private final ProductionInstructService productionInstructService;

    /**
     * 작업지시 목록 조회 API
     * 검색조건 : 제품명, 작업지시 번호
     * 페이징 조건 포함
     */
    public Page<ProductionInstructDTO> getProductionInstructList(Pageable pageable,
                                                                 @RequestParam("keyword") String keyword){

        Page<ProductionInstructDTO> list = productionInstructService.getProductionInstructList(pageable, keyword);

        return list;
    }

    // 2. 작업지시 상세
    public Optional<ProductionInstructDetailDTO> getProductionInstructDetail(@RequestParam("instructId") Long instructId){

        Optional<ProductionInstructDetailDTO> detail = productionInstructService.getProductionInstructDetail(instructId);


        return detail;
    }

    // 3. 작업지시 등록
    public void insertProductionInstruct(@RequestBody ProductionInstructInsertDTO productionInstructInsertDTO,
                                         @AuthenticationPrincipal AccountLogin accountLogin){

        Long userId = accountLogin.getId();

        productionInstructService.saveProductionInstruct(productionInstructInsertDTO, userId);
    }

    // 4. 작업지시 취소
    public void deleteProductionInstruct(){

    }

    // 5. 불량 등록

    // 5. 작업 완료

}
