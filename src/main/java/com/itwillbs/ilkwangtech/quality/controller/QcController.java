package com.itwillbs.ilkwangtech.quality.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.quality.entity.QcItem;
import com.itwillbs.ilkwangtech.quality.service.QcItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/quality")
@RequiredArgsConstructor
public class QcController {

	private final QcItemService qcItemService;

	// 페이지 경로
    @GetMapping("/QcItemList")
    public String qcItemPage() {
        return "quality/QcItemList"; 
    }

    // 품질관리항목 리스트 데이터 반환
    @GetMapping("/api/qcItems")
    @ResponseBody
    public List<QcItem> getQcItems() {
        return qcItemService.findAllQcItems(); 
    }
    
    // 품질관리항목 추가
    @PostMapping("/api/qcItems")
    @ResponseBody // JSON 데이터를 반환하기 위해 필요
    public QcItem insertQcItem(@RequestBody QcItem qcItem) {
        return qcItemService.saveQcItem(qcItem); 
    }
    
    // 품질관리 대시보드
    @GetMapping("/QcDashboard")
    public String qcDashboardPage() {
        return "quality/QcDashboard"; 
    }
    
    // 폐기 이력 페이지
    @GetMapping("/DisposalList")
    public String qcDisposalPage() {
        return "quality/DisposalList"; 
    }
}












