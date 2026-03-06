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

    @JsonProperty("beforeItemId")
    private Long beforeItemId;

    @JsonProperty("afterItemId")
    private Long afterItemId;

    @JsonProperty("requiredQty")
    private Long requiredQty;

}
