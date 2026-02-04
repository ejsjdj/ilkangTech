package com.itwillbs.ilkwangtech.item.constant;

public enum ItemType {
    RAW_MATERIAL("원자재"),
    WIP("재공품"),
    FINISHED_GOODS("완제품");

    private final String label;

    ItemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return this.name();
    }
}
