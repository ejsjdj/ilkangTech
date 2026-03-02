package com.itwillbs.ilkwangtech.standard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BomDTO {

    private Long bomId;

    @JsonProperty("parentItemId")
    private Long parentItemId;

    @JsonProperty("childItemId")
    private Long childItemId;

    @JsonProperty("requiredQty")
    private Long requiredQty;

}
