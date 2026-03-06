-- ==============================================================
-- DROP (의존성 순서대로)
-- ==============================================================
DROP TABLE BOM;
DROP TABLE operation;
DROP TABLE order_item;
DROP TABLE sales_order;
DROP TABLE item;
DROP TABLE company;

-- ==============================================================
-- DDL
-- ==============================================================
CREATE TABLE Item (
                      item_id        NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      item_code      VARCHAR2(50)  NOT NULL UNIQUE,
                      item_name      VARCHAR2(255) NOT NULL,
                      item_type      INTEGER,
                      UOM            VARCHAR2(10),
                      standard_price NUMBER(15)
);

CREATE TABLE Company (
                         company_id    NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         company_code  VARCHAR2(50)  NOT NULL UNIQUE,
                         company_name  VARCHAR2(50)  NOT NULL,
                         company_type  INTEGER,
                         company_email VARCHAR2(255),
                         ceo_name      VARCHAR2(50),
                         tel_no        VARCHAR2(20)
);

CREATE TABLE Equipment (
                           id             NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           equipment_name VARCHAR2(50)
);

CREATE TABLE Operation (
                           operation_id   NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           operation_name VARCHAR2(50) NOT NULL,
                           work_center_id NUMBER(19) REFERENCES Equipment(id)
);

CREATE TABLE BOM (
                     bom_id         NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                     parent_item_id NUMBER(19) NOT NULL REFERENCES Item(item_id),
                     child_item_id  NUMBER(19) NOT NULL REFERENCES Item(item_id),
                     operation_id   NUMBER(19) REFERENCES Operation(operation_id),
                     require_qty    NUMBER(10,2) NOT NULL
);

CREATE TABLE sales_order (
                             sales_order_id          NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             company_id              NUMBER(19) NOT NULL REFERENCES Company(company_id),
                             order_date              DATE DEFAULT SYSDATE,
                             expected_delivery_date  DATE,
                             order_status            VARCHAR2(20) DEFAULT 'PENDING'
);

CREATE TABLE order_item (
                            order_item_id  NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            sales_order_id NUMBER(19) NOT NULL REFERENCES sales_order(sales_order_id),
                            item_id        NUMBER(19) NOT NULL REFERENCES Item(item_id),
                            quantity       NUMBER(10) NOT NULL,
                            unit_price     NUMBER(19) NOT NULL
);

-- ==============================================================
-- 1. Item 기초 데이터
-- ==============================================================

-- [완제품 7종]
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-001', '삼성 미니 건조기 DV90TA040WH(화이트)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-002', '삼성 미니 건조기 DV90TA041DK(다크 그레이)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-003', '삼성 미니 건조기 DV90TA042KE(체리 레드)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-004', '삼성 AI 건조기 대용량 DV21DG8601GW(그랜드 화이트)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-005', '삼성 AI 건조기 대용량 DV21DG8602SB(스페이스 블랙)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-006', '삼성 AI 건조기 대용량 DV21DG8603SS(사일런트 실버)', 3, 'EA');
INSERT INTO Item (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('SAM-007', '삼성 AI 그랑데 데코레이션스페셜 DV19DK9088CH(샴페인)', 3, 'EA');

-- [원자재: 코일]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-001', 'Hot Rolled Coil', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-002', 'Cold Rolled Coil', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-003', 'Galvanized Coil', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-004', 'Colored Coil', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-005', 'Stainless Steel Coil', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-006', 'Aluminum Coil', 1, 'KG');

-- [원자재: 도료]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-007', 'WHITE - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-008', 'DARK GRAY - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-009', 'CHERRY RED - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-010', 'GRAND WHITE - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-011', 'SPACE BLACK - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-012', 'SILVER - HYDRID', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-013', 'CHAMPANGE - HYDRID', 1, 'KG');

-- [원자재: 레진]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-014', 'RESIN - WHITE', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-015', 'RESIN - DARK GRAY', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-016', 'RESIN - CHERRY RED', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-017', 'RESIN - GRAND WHITE', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-018', 'RESIN - SPACE BLACK', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-019', 'RESIN - SILVER', 1, 'KG');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-020', 'RESIN - CHAMPAGNE', 1, 'KG');

-- [원자재: 공용부자재]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-501', 'hinge-door', 1, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-502', 'bolt', 1, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-503', 'packing', 1, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('RW-504', 'board', 1, 'EA');

-- [중간제품: 프레스]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-001', 'SAMSUNG-MINI FRONT', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-002', 'SAMSUNG-MINI BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-003', 'SAMSUNG-MINI TOP COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-004', 'SAMSUNG-MINI LEFT COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-005', 'SAMSUNG-MINI RIGHT COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-006', 'SAMSUNG-MINI BOTTOM COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-007', 'SAMSUNG-MINI BACKET WRAPPER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-008', 'SAMSUNG-MINI BACKET BOWL', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-009', 'SAMSUNG-GRANDE FRONT', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-010', 'SAMSUNG-GRANDE BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-011', 'SAMSUNG-GRANDE TOP COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-012', 'SAMSUNG-GRANDE LEFT COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-013', 'SAMSUNG-GRANDE RIGHT COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-014', 'SAMSUNG-GRANDE BOTTOM COVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-015', 'SAMSUNG-GRANDE BACKET WRAPPER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('ST-016', 'SAMSUNG-GRANDE BACKET BOWL', 2, 'EA');

-- [중간제품: 사출품]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-001', 'SAMSUNG-MINI DOOR-FRONT WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-002', 'SAMSUNG-MINI DOOR-FRONT DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-003', 'SAMSUNG-MINI DOOR-FRONT CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-004', 'SAMSUNG-MINI DOOR-BACK WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-005', 'SAMSUNG-MINI DOOR-BACK DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-006', 'SAMSUNG-MINI DOOR-BACK CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-007', 'SAMSUNG-MINI DUST-TRAY WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-008', 'SAMSUNG-MINI DUST-TRAY DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-009', 'SAMSUNG-MINI DUST-TRAY CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-010', 'SAMSUNG-MINI DETERGENT BOX WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-011', 'SAMSUNG-MINI DETERGENT BOX DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-012', 'SAMSUNG-MINI DETERGENT BOX CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-013', 'SAMSUNG-MINI BALANCE TOP (COMMON)', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-014', 'SAMSUNG-MINI BALANCE BOTTOM (COMMON)', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-015', 'SAMSUNG-MINI STEAM FAN (COMMON)', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-016', 'SAMSUNG-GRANDE DOOR-FRONT GRANDE-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-017', 'SAMSUNG-GRANDE DOOR-FRONT SPACE-BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-018', 'SAMSUNG-GRANDE DOOR-FRONT SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-019', 'SAMSUNG-GRANDE DOOR-FRONT CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-020', 'SAMSUNG-GRANDE DOOR-BACK GRANDE-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-021', 'SAMSUNG-GRANDE DOOR-BACK SPACE-BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-022', 'SAMSUNG-GRANDE DOOR-BACK SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-023', 'SAMSUNG-GRANDE DOOR-BACK CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-024', 'SAMSUNG-GRANDE DUST-TRAY GRANDE-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-025', 'SAMSUNG-GRANDE DUST-TRAY SPACE-BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-026', 'SAMSUNG-GRANDE DUST-TRAY SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-027', 'SAMSUNG-GRANDE DUST-TRAY CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-028', 'SAMSUNG-GRANDE DETERGENT BOX GRANDE-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-029', 'SAMSUNG-GRANDE DETERGENT BOX SPACE-BACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-030', 'SAMSUNG-GRANDE DETERGENT BOX SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-031', 'SAMSUNG-GRANDE DETERGENT BOX CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-032', 'SAMSUNG-GRANDE BALANCE TOP (COMMON)', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-033', 'SAMSUNG-GRANDE BALANCE BOTTOM (COMMON)', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('IN-034', 'SAMSUNG-GRANDE STEAM FAN (COMMON)', 2, 'EA');

-- [중간제품: 도장품 PT-*]
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-101', 'SAMSUNG-MINI FRONT WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-102', 'SAMSUNG-MINI BACK WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-103', 'SAMSUNG-MINI TOP COVER WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-104', 'SAMSUNG-MINI LEFT COVER WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-105', 'SAMSUNG-MINI RIGHT COVER WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-106', 'SAMSUNG-MINI BOTTOM COVER WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-201', 'SAMSUNG-MINI FRONT DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-202', 'SAMSUNG-MINI BACK DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-203', 'SAMSUNG-MINI TOP COVER DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-204', 'SAMSUNG-MINI LEFT COVER DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-205', 'SAMSUNG-MINI RIGHT COVER DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-206', 'SAMSUNG-MINI BOTTOM COVER DARK-GRAY', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-301', 'SAMSUNG-MINI FRONT CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-302', 'SAMSUNG-MINI BACK CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-303', 'SAMSUNG-MINI TOP COVER CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-304', 'SAMSUNG-MINI LEFT COVER CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-305', 'SAMSUNG-MINI RIGHT COVER CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-306', 'SAMSUNG-MINI BOTTOM COVER CHERRY-RED', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-409', 'SAMSUNG-GRANDE FRONT GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-410', 'SAMSUNG-GRANDE BACK GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-411', 'SAMSUNG-GRANDE TOP COVER GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-412', 'SAMSUNG-GRANDE LEFT COVER GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-413', 'SAMSUNG-GRANDE RIGHT COVER GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-414', 'SAMSUNG-GRANDE BOTTOM COVER GRAND-WHITE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-509', 'SAMSUNG-GRANDE FRONT SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-510', 'SAMSUNG-GRANDE BACK SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-511', 'SAMSUNG-GRANDE TOP COVER SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-512', 'SAMSUNG-GRANDE LEFT COVER SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-513', 'SAMSUNG-GRANDE RIGHT COVER SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-514', 'SAMSUNG-GRANDE BOTTOM COVER SPACE-BLACK', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-609', 'SAMSUNG-GRANDE FRONT SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-610', 'SAMSUNG-GRANDE BACK SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-611', 'SAMSUNG-GRANDE TOP COVER SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-612', 'SAMSUNG-GRANDE LEFT COVER SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-613', 'SAMSUNG-GRANDE RIGHT COVER SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-614', 'SAMSUNG-GRANDE BOTTOM COVER SILVER', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-709', 'SAMSUNG-GRANDE FRONT CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-710', 'SAMSUNG-GRANDE BACK CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-711', 'SAMSUNG-GRANDE TOP COVER CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-712', 'SAMSUNG-GRANDE LEFT COVER CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-713', 'SAMSUNG-GRANDE RIGHT COVER CHAMPAGNE', 2, 'EA');
INSERT INTO ITEM (ITEM_CODE, ITEM_NAME, ITEM_TYPE, UOM) VALUES ('PT-714', 'SAMSUNG-GRANDE BOTTOM COVER CHAMPAGNE', 2, 'EA');

-- ==============================================================
-- 2. Company 데이터 (수정 반영)
-- ==============================================================

-- [고객사 - company_type = 3]
INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('CUST-001', '삼성전자', 3, 'purchase@samsung.com', '한종희', '02-2255-0114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('CUST-002', 'LG전자', 3, 'order@lge.com', '조주완', '02-3777-1114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('CUST-003', '위니아', 3, 'buy@winia.com', '박영우', '031-620-9000');

-- [공급업체 - company_type = 1]
INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('SUPP-001', '포스코', 1, 'sales@posco.com', '장인화', '054-220-0114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('SUPP-002', '현대제철', 1, 'supply@hyundai-steel.com', '안동일', '041-680-0114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('SUPP-003', 'LG화학', 1, 'resin@lgchem.com', '신학철', '02-3773-1114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('SUPP-004', '조광페인트', 1, 'paint@chokwang.co.kr', '이장우', '032-570-3114');

INSERT INTO Company (company_code, company_name, company_type, company_email, CEO_name, TEL_NO)
VALUES ('SUPP-005', '성광유니텍', 1, 'parts@sungkwang.co.kr', '김성광', '051-831-0001');

-- ==============================================================
-- 3. BOM 연결
-- ==============================================================

-- [1단계] 코일 -> 프레스
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 2.0 FROM Item p, Item c WHERE p.item_code = 'RW-002' AND c.item_code IN ('ST-001', 'ST-002');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.5 FROM Item p, Item c WHERE p.item_code = 'RW-002' AND c.item_code IN ('ST-003', 'ST-004', 'ST-005', 'ST-006');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 3.0 FROM Item p, Item c WHERE p.item_code = 'RW-005' AND c.item_code IN ('ST-007', 'ST-008');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 3.5 FROM Item p, Item c WHERE p.item_code = 'RW-002' AND c.item_code IN ('ST-009', 'ST-010');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 2.0 FROM Item p, Item c WHERE p.item_code = 'RW-002' AND c.item_code IN ('ST-011', 'ST-012', 'ST-013', 'ST-014');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 4.5 FROM Item p, Item c WHERE p.item_code = 'RW-005' AND c.item_code IN ('ST-015', 'ST-016');

-- [2단계] 레진 -> 사출품
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-014' AND c.item_code IN ('IN-001','IN-004','IN-007','IN-010','IN-013','IN-014','IN-015');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-015' AND c.item_code IN ('IN-002','IN-005','IN-008','IN-011');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-016' AND c.item_code IN ('IN-003','IN-006','IN-009','IN-012');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'RW-017' AND c.item_code IN ('IN-016','IN-020','IN-024','IN-028','IN-032','IN-033','IN-034');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'RW-018' AND c.item_code IN ('IN-017','IN-021','IN-025','IN-029');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'RW-019' AND c.item_code IN ('IN-018','IN-022','IN-026','IN-030');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'RW-020' AND c.item_code IN ('IN-019','IN-023','IN-027','IN-031');

-- [3단계] 프레스 + 도료 -> 도장품
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-001' AND c.item_code IN ('PT-101','PT-201','PT-301');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-002' AND c.item_code IN ('PT-102','PT-202','PT-302');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-003' AND c.item_code IN ('PT-103','PT-203','PT-303');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-004' AND c.item_code IN ('PT-104','PT-204','PT-304');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-005' AND c.item_code IN ('PT-105','PT-205','PT-305');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-006' AND c.item_code IN ('PT-106','PT-206','PT-306');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-009' AND c.item_code IN ('PT-409','PT-509','PT-609','PT-709');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-010' AND c.item_code IN ('PT-410','PT-510','PT-610','PT-710');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-011' AND c.item_code IN ('PT-411','PT-511','PT-611','PT-711');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-012' AND c.item_code IN ('PT-412','PT-512','PT-612','PT-712');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-013' AND c.item_code IN ('PT-413','PT-513','PT-613','PT-713');
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code = 'ST-014' AND c.item_code IN ('PT-414','PT-514','PT-614','PT-714');

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-007' AND c.item_code LIKE 'PT-10_';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-008' AND c.item_code LIKE 'PT-20_';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.5 FROM Item p, Item c WHERE p.item_code = 'RW-009' AND c.item_code LIKE 'PT-30_';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.8 FROM Item p, Item c WHERE p.item_code = 'RW-010' AND c.item_code LIKE 'PT-4__';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.8 FROM Item p, Item c WHERE p.item_code = 'RW-011' AND c.item_code LIKE 'PT-5__';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.8 FROM Item p, Item c WHERE p.item_code = 'RW-012' AND c.item_code LIKE 'PT-6__';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 0.8 FROM Item p, Item c WHERE p.item_code = 'RW-013' AND c.item_code LIKE 'PT-7__';

-- [4단계] 완제품 BOM
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-101','PT-102','PT-103','PT-104','PT-105','PT-106','ST-007','ST-008','IN-001','IN-004','IN-007','IN-010','IN-013','IN-014','IN-015','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-001';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 20.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-001';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-201','PT-202','PT-203','PT-204','PT-205','PT-206','ST-007','ST-008','IN-002','IN-005','IN-008','IN-011','IN-013','IN-014','IN-015','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-002';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 20.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-002';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-301','PT-302','PT-303','PT-304','PT-305','PT-306','ST-007','ST-008','IN-003','IN-006','IN-009','IN-012','IN-013','IN-014','IN-015','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-003';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 20.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-003';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-409','PT-410','PT-411','PT-412','PT-413','PT-414','ST-015','ST-016','IN-016','IN-020','IN-024','IN-028','IN-032','IN-033','IN-034','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-004';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 30.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-004';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-509','PT-510','PT-511','PT-512','PT-513','PT-514','ST-015','ST-016','IN-017','IN-021','IN-025','IN-029','IN-032','IN-033','IN-034','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-005';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 30.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-005';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-609','PT-610','PT-611','PT-612','PT-613','PT-614','ST-015','ST-016','IN-018','IN-022','IN-026','IN-030','IN-032','IN-033','IN-034','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-006';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 30.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-006';

INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 1.0 FROM Item p, Item c WHERE p.item_code IN ('PT-709','PT-710','PT-711','PT-712','PT-713','PT-714','ST-015','ST-016','IN-019','IN-023','IN-027','IN-031','IN-032','IN-033','IN-034','RW-501','RW-503','RW-504') AND c.item_code = 'SAM-007';
INSERT INTO BOM (parent_item_id, child_item_id, require_qty) SELECT p.item_id, c.item_id, 30.0 FROM Item p, Item c WHERE p.item_code = 'RW-502' AND c.item_code = 'SAM-007';

-- ==============================================================
-- 4. 수주 테스트 데이터 (서브쿼리 방식 - 안전)
-- ==============================================================
INSERT INTO sales_order (company_id, order_date, expected_delivery_date)
SELECT company_id, SYSDATE, SYSDATE + 10
FROM Company WHERE company_code = 'CUST-001';

INSERT INTO order_item (sales_order_id, item_id, quantity, unit_price)
SELECT
    (SELECT sales_order_id FROM sales_order
     WHERE company_id = (SELECT company_id FROM Company WHERE company_code = 'CUST-001')
       AND ROWNUM = 1),
    item_id, 10, 850000
FROM Item WHERE item_code = 'SAM-001';

COMMIT;
