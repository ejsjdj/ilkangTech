package com.itwillbs.ilkwangtech.standard.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

/**
 * bom 을 이루는 아이템들을 가져올때
 * 해당 정보를 담을 DTO
 */
@ToString
@Getter
@Setter
public class ParentItemDTO {

    private Long itemId;
    private String code;
    private String name;
    private Long unit;

}
