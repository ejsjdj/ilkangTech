package com.itwillbs.ilkwangtech.quality.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.ilkwangtech.quality.entity.QcItem;
import com.itwillbs.ilkwangtech.quality.service.QcItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/quality")
@RequiredArgsConstructor
public class QcItemController {

	private final QcItemService qcItemService;

	// 페이지 경로
    @GetMapping("/qcItemList")
    public String qcItemPage() {
        return "quality/qcItemList"; 
    }

    // 품질관리항목 리스트 데이터 반환
    @GetMapping("/api/qcItems")
    @ResponseBody
    public List<QcItem> getQcItems() {
        return qcItemService.findAllQcItems(); 
    }
}












