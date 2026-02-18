package com.itwillbs.ilkwangtech.item.dto;

import com.itwillbs.ilkwangtech.item.constant.ItemType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemDTO {

    Integer id; // 1. 제품 ID

    String itemId; // 2. 품번(A-20260213-100)

    String itemName; // 3. 품명(알루미늄 철판 30g)

    String unit; // 4. 단위(kg, ml, cm 등등)

    Long price; // 5. 단가(100)

    // getLabel() -> 중간제품, 완제품, 원자재, 부자재, 포장재
    // getId() -> 1, 2, 3
    ItemType type; // 6. 제품 유형

    String companyId; // 7. 거래처/판매처(PT-20250101-121)

    String description; // 8. 제품설명(건조기 먼지필터 덮게)

}