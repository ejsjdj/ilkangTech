package com.itwillbs.ilkwangtech.quality.dto;

public interface DisposalHistoryDto {
    Long getId();
    String getLotNo();
    String getItemName();
    String getProcessName();
    String getMemberName();
    Integer getDisposalQty();
    String getDisposalReason();
    String getWorkDate();
}
