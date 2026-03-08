package com.itwillbs.ilkwangtech.quality.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectReasonRequest {
    private Long workerId;
    private String rejectReason;
}
