package com.itwillbs.ilkwangtech.item.constant;

public enum ItemType {
    RAW(1, "원자재"),
    WIP(2, "재공품"),
    FG(3, "제품");

    private final int id;      // 숫자 코드
    private final String label; // 한글 명칭

    // 생성자
    ItemType(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return this.name(); // "RAW", "WIP" 등 반환
    }

   public static ItemType fromId(int id) {
        for (ItemType type : ItemType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null; // 또는 예외 발생
    }
}
