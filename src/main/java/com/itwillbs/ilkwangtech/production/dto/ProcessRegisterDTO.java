package com.itwillbs.ilkwangtech.production.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessRegisterDTO {

    private Long sequence;
    private Long processId;
    private String processCode;
    private String processName;

}
