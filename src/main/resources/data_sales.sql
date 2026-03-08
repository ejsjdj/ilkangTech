-- =================================================================================
-- [재고 관리] 보유 재고 추가 (INSERT) 예시
-- =================================================================================

/*
-- [참고] inventory 테이블 구조 (JPA 엔티티 기반)
CREATE TABLE inventory (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id NUMBER(19) NOT NULL REFERENCES Item(item_id),
    lot_number VARCHAR2(100) UNIQUE,
    zone VARCHAR2(50),
    rack VARCHAR2(50),
    current_quantity NUMBER(19),
    expiration_date DATE
);
*/

-- 1. 완제품(FG) 재고 추가 (예시: item_id=1, 2)
-- LOT 번호는 중복되지 않도록 고유하게 부여해야 합니다.
INSERT INTO inventory (item_id, lot_number, zone, rack, current_quantity, expiration_date)
VALUES (1, 'LOT-20260309-001', 'ZONE A', 'Rack 01', 100, TO_DATE('2026-12-31', 'YYYY-MM-DD'));

INSERT INTO inventory (item_id, lot_number, zone, rack, current_quantity, expiration_date)
VALUES (1, 'LOT-20260309-002', 'ZONE A', 'Rack 02', 50, TO_DATE('2026-06-30', 'YYYY-MM-DD'));

INSERT INTO inventory (item_id, lot_number, zone, rack, current_quantity, expiration_date)
VALUES (2, 'LOT-20260309-003', 'ZONE B', 'Rack 05', 200, TO_DATE('2027-01-15', 'YYYY-MM-DD'));

-- 2. 원자재(RAW) 재고 추가 (예시: item_id=8, 9)
INSERT INTO inventory (item_id, lot_number, zone, rack, current_quantity, expiration_date)
VALUES (8, 'LOT-RAW-2026-001', 'ZONE C', 'Rack 10', 1000, TO_DATE('2028-12-31', 'YYYY-MM-DD'));

-- 작업 후 COMMIT 필수
COMMIT;
