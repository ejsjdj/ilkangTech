package com.itwillbs.ilkwangtech.standard.dto;

import com.itwillbs.ilkwangtech.standard.constant.ProcessStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessStatusUpdateDTO {
    private Long processId;
    private String operationCode;
    private String name;
    private String description;
    private ProcessStatus status;
}
