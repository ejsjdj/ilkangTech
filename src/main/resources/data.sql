-- =================================================================================
-- [0] 시퀀스 초기화 및 생성 (Oracle 환경에서 data.sql 재실행 시 충돌 방지)
-- =================================================================================
-- 이미 존재하는 경우 삭제 후 재생성하여 ORA-00955 에러를 방지합니다.


DROP SEQUENCE draft_approval_line_seq;
DROP SEQUENCE draft_document_seq;
DROP SEQUENCE leave_status_seq;
DROP SEQUENCE member_role_seq;
DROP SEQUENCE common_code_seq;
DROP SEQUENCE MEMBERS_SEQ;
DROP SEQUENCE SEQ_SCHEDULE;
DROP SEQUENCE SEQ_ATTENDANCE;
DROP SEQUENCE SEQ_NOTICE;

CREATE SEQUENCE draft_approval_line_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE draft_document_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE leave_status_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE member_role_seq START WITH 200 INCREMENT BY 1;
CREATE SEQUENCE common_code_seq START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE MEMBERS_SEQ START WITH 200 INCREMENT BY 1;
CREATE SEQUENCE SEQ_SCHEDULE START WITH 200 INCREMENT BY 1;
CREATE SEQUENCE SEQ_ATTENDANCE START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SEQ_NOTICE START WITH 1 INCREMENT BY 1;

-- Common Code (사용자 권한, 메뉴, 게시판 등)
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, '', 'MEMBER_ROLE', '사용자권한', '사용자권한 상위코드', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'MEMBER_ROLE', 'ROLE_ADMIN', '전체 관리자 권한', '', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'MEMBER_ROLE', 'ROLE_USER', '일반 사용자 권한', '', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'MEMBER_ROLE', 'ROLE_ADMIN_SUB', '보조 관리자 권한', '', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, '', 'MENU', '시스템 메뉴', '시스템 메뉴 상위코드', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'MENU', '1', '메뉴 - 공지사항', '', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'MENU', '2', '메뉴 - 상품페이지', '', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, '', 'BOARD', '게시판', '게시판 상위코드', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'BOARD', 'NOTICE', '공지사항', '게시판 - 공지사항', 'Y');
INSERT INTO common_code(id, group_code, common_code, common_code_name, description, use_yn) VALUES (common_code_seq.NEXTVAL, 'BOARD', 'FREE', '자유게시판', '게시판 - 자유게시판', 'Y');

-- Genders (성별)
INSERT INTO genders (id, gender) VALUES (1, '남자');
INSERT INTO genders (id, gender) VALUES (2, '여자');

-- Banks (은행)
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (4, '004', '국민은행', 'Kookmin Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (20, '020', '우리은행', 'Woori Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (88, '088', '신한은행', 'Shinhan Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (81, '081', '하나은행', 'Hana Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (11, '011', '농협은행', 'NH Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (23, '023', 'SC제일은행', 'SC First Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (27, '027', '시티은행', 'Citibank Korea', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (90, '090', '카카오뱅크', 'Kakao Bank', '인터넷은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (92, '092', '토스뱅크', 'Toss Bank', '인터넷은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (48, '048', '신용협동조합', 'Credit Union', '기타금융', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (45, '045', '새마을금고', 'Saemaul Geumgo', '기타금융', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active) VALUES (2, '002', '산업은행', 'Korea Development Bank', '기타금융', 1);

-- Departments (부서)
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (0, '임원', null, 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (1, '관리부', '0', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (2, '인사팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (3, '구매팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (4, '영업팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (5, '재무회계팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (6, '정보시스템팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (7, '경영팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (8, '안전팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (9, '법무팀', '1', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (100, '생산부', '0', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (101, '프레스팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (102, '사출팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (103, '도장팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (104, '조립팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (105, '품질팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (106, '금형팀', '100', 1);
INSERT INTO departments (id, department_name, parent_department, is_active) VALUES (107, '생산관리팀', '100', 1);

-- Positions (직급)
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (1, '부장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (2, '차장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (3, '과장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (4, '대리', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (5, '주임', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (6, '사원', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (51, '이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (52, '상무이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (53, '전무이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (54, '부사장', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (55, '사장', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (56, '대표이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (101, '기능공', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (102, '기능사', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (103, '선임기능사', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (104, '기능장', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active) VALUES (105, '수석기능장', '기술직', 1);

COMMIT;

-- =================================================================================
-- [2] 메인 데이터 (Members)
-- =================================================================================

INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (1, '나대표', '26-00000', 1, TO_DATE('2026-01-01', 'YYYY-MM-DD'), '700101-1234567', 'ceo@ilkang.com', '010-1111-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 0, 56, 88, '110-123-456789', SYSDATE, SYSDATE);

-- 관리부 (id: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (2, '김관리', '26-10000', 1, TO_DATE('2026-01-05', 'YYYY-MM-DD'), '800505-1234567', 'admin@ilkang.com', '010-2222-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 1, 1, 4, '123-456-789012', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (4, '이관리', '26-10001', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '850101-2345678', 'lee_admin@ilkang.com', '010-2222-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 1, 2, 4, '123-456-789013', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (5, '박관리', '26-10002', 1, TO_DATE('2026-02-01', 'YYYY-MM-DD'), '900202-1234567', 'park_admin@ilkang.com', '010-2222-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 1, 4, 4, '123-456-789014', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (50, '정관리', '26-10003', 2, TO_DATE('2026-02-15', 'YYYY-MM-DD'), '920303-2345678', 'jung_admin@ilkang.com', '010-2222-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 1, 4, 4, '123-456-789015', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (51, '최관리', '26-10004', 1, TO_DATE('2026-03-01', 'YYYY-MM-DD'), '940404-1234567', 'choi_admin@ilkang.com', '010-2222-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 1, 5, 4, '123-456-789016', SYSDATE, SYSDATE);

-- 인사팀 (id: 2, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (11, '이인사', '26-11000', 2, TO_DATE('2026-01-10', 'YYYY-MM-DD'), '900101-2345678', 'hr@ilkang.com', '010-3333-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 3, 20, '1002-123-456789', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (12, '박사원', '26-11001', 1, TO_DATE('2026-02-01', 'YYYY-MM-DD'), '950505-1234567', 'park@ilkang.com', '010-4444-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 6, 81, '333-444444-55555', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (31, '김인사', '26-11002', 1, TO_DATE('2026-03-01', 'YYYY-MM-DD'), '920303-1234567', 'kim_hr@ilkang.com', '010-3333-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 4, 20, '1002-123-456790', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (32, '최인사', '26-11003', 2, TO_DATE('2026-03-15', 'YYYY-MM-DD'), '940404-2345678', 'choi_hr@ilkang.com', '010-3333-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 5, 20, '1002-123-456791', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (52, '윤인사', '26-11004', 1, TO_DATE('2026-04-01', 'YYYY-MM-DD'), '910505-1234567', 'yoon_hr@ilkang.com', '010-3333-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 6, 20, '1002-123-456792', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (53, '강인사', '26-11005', 2, TO_DATE('2026-04-15', 'YYYY-MM-DD'), '930606-2345678', 'kang_hr@ilkang.com', '010-3333-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 2, 6, 20, '1002-123-456793', SYSDATE, SYSDATE);

-- 구매팀 (id: 3, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (33, '나구매', '26-13000', 1, TO_DATE('2026-01-20', 'YYYY-MM-DD'), '870101-1234567', 'buy@ilkang.com', '010-4444-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 3, 3, 81, '111-222-333333', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (34, '유사원', '26-13001', 2, TO_DATE('2026-02-15', 'YYYY-MM-DD'), '960202-2345678', 'yu@ilkang.com', '010-4444-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 3, 6, 81, '111-222-333334', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (54, '오구매', '26-13002', 1, TO_DATE('2026-03-20', 'YYYY-MM-DD'), '900303-1234567', 'oh_buy@ilkang.com', '010-4444-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 3, 4, 81, '111-222-333335', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (55, '전구매', '26-13003', 2, TO_DATE('2026-04-10', 'YYYY-MM-DD'), '920404-2345678', 'jeon_buy@ilkang.com', '010-4444-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 3, 5, 81, '111-222-333336', SYSDATE, SYSDATE);

-- 영업팀 (id: 4, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (35, '왕영업', '26-14000', 1, TO_DATE('2026-01-25', 'YYYY-MM-DD'), '840505-1234567', 'sales@ilkang.com', '010-5555-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 4, 2, 88, '110-111-222222', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (36, '전대리', '26-14001', 1, TO_DATE('2026-02-20', 'YYYY-MM-DD'), '910606-1234567', 'jeon@ilkang.com', '010-5555-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 4, 4, 88, '110-111-222223', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (56, '배영업', '26-14002', 2, TO_DATE('2026-03-25', 'YYYY-MM-DD'), '880707-2345678', 'bae_sales@ilkang.com', '010-5555-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 4, 3, 88, '110-111-222224', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (57, '송영업', '26-14003', 1, TO_DATE('2026-04-05', 'YYYY-MM-DD'), '930808-1234567', 'song_sales@ilkang.com', '010-5555-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 4, 5, 88, '110-111-222225', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (58, '탁영업', '26-14004', 2, TO_DATE('2026-04-15', 'YYYY-MM-DD'), '950909-2345678', 'tak_sales@ilkang.com', '010-5555-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 4, 6, 88, '110-111-222226', SYSDATE, SYSDATE);

-- 재무회계팀 (id: 5, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (13, '최재무', '26-12000', 1, TO_DATE('2026-01-12', 'YYYY-MM-DD'), '850303-1234567', 'finance@ilkang.com', '010-5555-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 3, 11, '302-1234-5678-91', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (37, '고재무', '26-12001', 2, TO_DATE('2026-02-10', 'YYYY-MM-DD'), '890707-2345678', 'ko_finance@ilkang.com', '010-5555-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 4, 11, '302-1234-5678-92', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (59, '지재무', '26-12002', 1, TO_DATE('2026-03-05', 'YYYY-MM-DD'), '900808-1234567', 'jee_finance@ilkang.com', '010-5555-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 5, 11, '302-1234-5678-93', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (60, '허재무', '26-12003', 2, TO_DATE('2026-04-20', 'YYYY-MM-DD'), '930909-2345678', 'hur_finance@ilkang.com', '010-5555-8888', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 11, '302-1234-5678-94', SYSDATE, SYSDATE);

-- 정보시스템팀 (id: 6, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (38, '한정보', '26-16000', 1, TO_DATE('2026-01-30', 'YYYY-MM-DD'), '860808-1234567', 'it@ilkang.com', '010-6666-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 6, 3, 90, '3333-02-1234567', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (39, '임사원', '26-16001', 1, TO_DATE('2026-02-25', 'YYYY-MM-DD'), '930909-1234567', 'lim@ilkang.com', '010-6666-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 6, 6, 90, '3333-02-1234568', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (61, '서정보', '26-16002', 1, TO_DATE('2026-03-10', 'YYYY-MM-DD'), '911010-1234567', 'seo_it@ilkang.com', '010-6666-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 6, 4, 90, '3333-02-1234569', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (62, '권정보', '26-16003', 2, TO_DATE('2026-03-20', 'YYYY-MM-DD'), '941111-2345678', 'kwon_it@ilkang.com', '010-6666-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 6, 5, 90, '3333-02-1234570', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (63, '유정보', '26-16004', 1, TO_DATE('2026-04-05', 'YYYY-MM-DD'), '961212-1234567', 'yoo_it@ilkang.com', '010-6666-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 6, 6, 90, '3333-02-1234571', SYSDATE, SYSDATE);

-- 경영팀 (id: 7, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (40, '문경영', '26-17000', 2, TO_DATE('2026-01-05', 'YYYY-MM-DD'), '831010-2345678', 'mgt@ilkang.com', '010-7777-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 7, 2, 4, '123-111-222222', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (64, '노경영', '26-17001', 1, TO_DATE('2026-02-15', 'YYYY-MM-DD'), '861111-1234567', 'roh_mgt@ilkang.com', '010-7777-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 7, 3, 4, '123-111-222223', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (65, '신경영', '26-17002', 2, TO_DATE('2026-03-25', 'YYYY-MM-DD'), '901212-2345678', 'shin_mgt@ilkang.com', '010-7777-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 7, 4, 4, '123-111-222224', SYSDATE, SYSDATE);

-- 안전팀 (id: 8, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (41, '오안전', '26-18000', 1, TO_DATE('2026-01-10', 'YYYY-MM-DD'), '811111-1234567', 'safety@ilkang.com', '010-8888-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 8, 3, 11, '302-3333-4444-55', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (66, '염안전', '26-18001', 1, TO_DATE('2026-02-20', 'YYYY-MM-DD'), '851212-1234567', 'yeom_safety@ilkang.com', '010-8888-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 8, 4, 11, '302-3333-4444-56', SYSDATE, SYSDATE);

-- 법무팀 (id: 9, parent: 1)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (42, '조법무', '26-19000', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '821212-1234567', 'legal@ilkang.com', '010-9999-1111', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 9, 3, 88, '110-333-444444', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (67, '심법무', '26-19001', 2, TO_DATE('2026-03-05', 'YYYY-MM-DD'), '880101-2345678', 'shim_legal@ilkang.com', '010-9999-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 9, 4, 88, '110-333-444445', SYSDATE, SYSDATE);

-- 생산부 (id: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (3, '박생산', '26-20000', 1, TO_DATE('2026-01-05', 'YYYY-MM-DD'), '820808-1234567', 'prod_head@ilkang.com', '010-6666-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 100, 1, 88, '110-987-654321', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (68, '양생산', '26-20001', 1, TO_DATE('2026-02-10', 'YYYY-MM-DD'), '850909-1234567', 'yang_prod@ilkang.com', '010-6666-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 100, 2, 88, '110-987-654322', SYSDATE, SYSDATE);

-- 프레스팀 (id: 101, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (21, '정프레스', '26-21000', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '880101-1234567', 'press@ilkang.com', '010-7777-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 101, 104, 4, '001-000-21000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (22, '강사원', '26-21001', 1, TO_DATE('2026-02-10', 'YYYY-MM-DD'), '980101-1234567', 'kang@ilkang.com', '010-8888-8888', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 101, 101, 4, '001-000-21001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (43, '손기술', '26-21002', 1, TO_DATE('2026-03-05', 'YYYY-MM-DD'), '950101-1234567', 'son@ilkang.com', '010-7777-8888', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 101, 102, 4, '001-000-21002', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (69, '주프레스', '26-21003', 1, TO_DATE('2026-03-15', 'YYYY-MM-DD'), '920202-1234567', 'joo_press@ilkang.com', '010-7777-9999', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 101, 101, 4, '001-000-21003', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (70, '임프레스', '26-21004', 1, TO_DATE('2026-04-01', 'YYYY-MM-DD'), '940303-1234567', 'lim_press@ilkang.com', '010-7777-0000', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 101, 101, 4, '001-000-21004', SYSDATE, SYSDATE);

-- 사출팀 (id: 102, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (44, '홍사출', '26-22000', 1, TO_DATE('2026-01-20', 'YYYY-MM-DD'), '890202-1234567', 'injection@ilkang.com', '010-8888-2222', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 102, 103, 11, '302-000-22000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (71, '안사출', '26-22001', 1, TO_DATE('2026-02-25', 'YYYY-MM-DD'), '910303-1234567', 'ahn_injection@ilkang.com', '010-8888-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 102, 101, 11, '302-000-22001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (72, '성사출', '26-22002', 1, TO_DATE('2026-03-20', 'YYYY-MM-DD'), '930404-1234567', 'sung_injection@ilkang.com', '010-8888-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 102, 102, 11, '302-000-22002', SYSDATE, SYSDATE);

-- 도장팀 (id: 103, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (45, '백도장', '26-23000', 1, TO_DATE('2026-01-25', 'YYYY-MM-DD'), '870303-1234567', 'painting@ilkang.com', '010-9999-3333', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 103, 103, 20, '1002-000-23000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (73, '문도장', '26-23001', 1, TO_DATE('2026-02-15', 'YYYY-MM-DD'), '900404-1234567', 'moon_painting@ilkang.com', '010-9999-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 103, 101, 20, '1002-000-23001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (74, '신도장', '26-23002', 1, TO_DATE('2026-04-05', 'YYYY-MM-DD'), '950505-1234567', 'shin_painting@ilkang.com', '010-9999-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 103, 102, 20, '1002-000-23002', SYSDATE, SYSDATE);

-- 조립팀 (id: 104, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (46, '황조립', '26-24000', 1, TO_DATE('2026-01-30', 'YYYY-MM-DD'), '860404-1234567', 'assembly@ilkang.com', '010-1111-4444', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 104, 103, 81, '111-000-24000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (75, '구조립', '26-24001', 1, TO_DATE('2026-03-01', 'YYYY-MM-DD'), '920505-1234567', 'koo_assembly@ilkang.com', '010-1111-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 104, 101, 81, '111-000-24001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (76, '권조립', '26-24002', 1, TO_DATE('2026-03-15', 'YYYY-MM-DD'), '940606-1234567', 'kwon_assembly@ilkang.com', '010-1111-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 104, 101, 81, '111-000-24002', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (77, '유조립', '26-24003', 1, TO_DATE('2026-04-10', 'YYYY-MM-DD'), '960707-1234567', 'yoo_assembly@ilkang.com', '010-1111-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 104, 102, 81, '111-000-24003', SYSDATE, SYSDATE);

-- 품질팀 (id: 105, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (23, '윤품질', '26-25000', 2, TO_DATE('2026-01-20', 'YYYY-MM-DD'), '920202-2345678', 'qa@ilkang.com', '010-9999-9999', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 105, 3, 90, '3333-01-1234567', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (47, '노사원', '26-25001', 2, TO_DATE('2026-03-10', 'YYYY-MM-DD'), '990505-2345678', 'no@ilkang.com', '010-9999-8888', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 105, 6, 90, '3333-01-1234568', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (78, '한품질', '26-25002', 2, TO_DATE('2026-03-25', 'YYYY-MM-DD'), '950606-2345678', 'han_qa@ilkang.com', '010-9999-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 105, 4, 90, '3333-01-1234569', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (79, '우품질', '26-25003', 1, TO_DATE('2026-04-15', 'YYYY-MM-DD'), '970707-1234567', 'woo_qa@ilkang.com', '010-9999-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 105, 5, 90, '3333-01-1234570', SYSDATE, SYSDATE);

-- 금형팀 (id: 106, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (48, '신금형', '26-26000', 1, TO_DATE('2026-02-05', 'YYYY-MM-DD'), '840606-1234567', 'mold@ilkang.com', '010-2222-5555', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 106, 104, 88, '110-000-26000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (80, '방금형', '26-26001', 1, TO_DATE('2026-03-10', 'YYYY-MM-DD'), '900707-1234567', 'bang_mold@ilkang.com', '010-2222-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 106, 102, 88, '110-000-26001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (81, '최금형', '26-26002', 1, TO_DATE('2026-04-01', 'YYYY-MM-DD'), '930808-1234567', 'choi_mold@ilkang.com', '010-2222-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 106, 101, 88, '110-000-26002', SYSDATE, SYSDATE);

-- 생산관리팀 (id: 107, parent: 100)
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (49, '유생산', '26-27000', 1, TO_DATE('2026-02-10', 'YYYY-MM-DD'), '830707-1234567', 'pm@ilkang.com', '010-3333-6666', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 107, 3, 4, '123-000-27000', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (82, '조생산', '26-27001', 1, TO_DATE('2026-03-20', 'YYYY-MM-DD'), '890808-1234567', 'cho_pm@ilkang.com', '010-3333-7777', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 107, 4, 4, '123-000-27001', SYSDATE, SYSDATE);
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, email, phone_number, password, department, position, bank, account_number, last_login, updated_at) 
VALUES (83, '홍사원', '26-27002', 2, TO_DATE('2026-04-10', 'YYYY-MM-DD'), '960909-2345678', 'hong_pm@ilkang.com', '010-3333-8888', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 107, 6, 4, '123-000-27002', SYSDATE, SYSDATE);

COMMIT;

-- =================================================================================
-- [3] 연관 데이터 (Member Roles, Schedules, Drafts, etc.)
-- =================================================================================

-- Member Roles (모든 사원에게 ROLE_USER 권한 부여. ROLE_USER의 common_code id는 시퀀스에 의해 결정되므로 쿼리로 처리하거나 정확한 ID 명시 필요)
-- 여기서는 common_code 테이블에서 'ROLE_USER'의 ID를 조회하여 삽입하는 방식이 가장 안전함 (H2/Oracle 문법 고려)
INSERT INTO member_role(id, member_id, member_role_id)
SELECT member_role_seq.NEXTVAL, M.id, C.id 
FROM members M, common_code C 
WHERE C.common_code = 'ROLE_USER';

-- 전사 일정 등록
INSERT INTO IK_SCHEDULE (schedule_id, member_id, schedule_title, schedule_memo, schedule_type, start_date, end_date, reg_date)
SELECT SEQ_SCHEDULE.NEXTVAL, M.id, '1월 전사 정기 회의', '2026년 상반기 목표 공유 및 부서별 현황 발표', 'COMPANY', TO_DATE('2026-01-20 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-01-20 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), SYSDATE FROM members M;

-- 개인 일정 등록
INSERT INTO IK_SCHEDULE (schedule_id, member_id, schedule_title, schedule_memo, schedule_type, start_date, end_date, reg_date)
SELECT SEQ_SCHEDULE.NEXTVAL, M.id, '개인 업무 정리', '주간 업무 보고서 작성', 'PERSONAL', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-01-21 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), SYSDATE FROM members M;

-- 휴가 현황 초기화
INSERT INTO leave_status (leave_id, common_id, total_leave, used_leave, remain_leave)
SELECT leave_status_seq.NEXTVAL, M.id, 15, 0, 15 FROM members M;

-- 전자결재 문서 (Draft Documents)
INSERT INTO draft_document (draft_id, common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (draft_document_seq.NEXTVAL, 1, '휴가신청서', '연차 신청합니다.', '개인 사정으로 인한 연차 신청', 'download/illkang/vacation_request.pdf', TO_DATE('2026-01-21', 'YYYY-MM-DD'), TO_DATE('2026-06-21', 'YYYY-MM-DD'), '승인');
INSERT INTO draft_document (draft_id, common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (draft_document_seq.NEXTVAL, 1, '지출결의서', '비품 구매 건', '사무용품(A4용지 등) 구매', 'download/illkang/expense_report.docx', TO_DATE('2026-01-21', 'YYYY-MM-DD'), TO_DATE('2026-06-17', 'YYYY-MM-DD'), '승인');
INSERT INTO draft_document (draft_id, common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (draft_document_seq.NEXTVAL, 11, '휴가신청서', '동계 휴가 신청', '가족 여행으로 인한 휴가 신청', 'download/illkang/vacation_winter.pdf', TO_DATE('2026-02-10', 'YYYY-MM-DD'), TO_DATE('2026-02-13', 'YYYY-MM-DD'), '승인');
INSERT INTO draft_document (draft_id, common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (draft_document_seq.NEXTVAL, 21, '기안서', '프레스 설비 점검 요청', '노후 설비 정기 점검 및 부품 교체 건', 'download/illkang/press_maintenance.pdf', TO_DATE('2026-02-15', 'YYYY-MM-DD'), TO_DATE('2026-02-15', 'YYYY-MM-DD'), '대기');

-- 결재선 (Draft Approval Line) - common_id는 결재자를 의미하며 반드시 members 테이블에 존재해야 함
INSERT INTO draft_approval_line (line_id, draft_type, common_id, sequence) VALUES (draft_approval_line_seq.NEXTVAL, 'PTO', 1, 1);
INSERT INTO draft_approval_line (line_id, draft_type, common_id, sequence) VALUES (draft_approval_line_seq.NEXTVAL, 'PTO', 2, 2);
INSERT INTO draft_approval_line (line_id, draft_type, common_id, sequence) VALUES (draft_approval_line_seq.NEXTVAL, 'BUY', 1, 1);
INSERT INTO draft_approval_line (line_id, draft_type, common_id, sequence) VALUES (draft_approval_line_seq.NEXTVAL, 'BUY', 11, 2);

COMMIT;

-- [1] 고정 게시글 3개 등록 (is_pinned = 1)
INSERT INTO notice (id, title, content, writer_name, writer_rank, writer_dept, view_count, is_pinned, has_attachment, reg_date, mod_date)
VALUES (SEQ_NOTICE.NEXTVAL, '[중요] 전사 통합 ERP 시스템 점검 안내', '시스템 안정화를 위한 정기 점검이 예정되어 있습니다.', '이순신', '사장', '관리부', 120, 1, 1, TO_TIMESTAMP('2026-01-26 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), SYSDATE);

INSERT INTO notice (id, title, content, writer_name, writer_rank, writer_dept, view_count, is_pinned, has_attachment, reg_date, mod_date)
VALUES (SEQ_NOTICE.NEXTVAL, '[공지] 2026년 상반기 핵심 경영 목표 공유', '금일 전사 회의에서 발표된 경영 목표 자료입니다.', '이순신', '사장', '관리부', 350, 1, 1, TO_TIMESTAMP('2026-01-25 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), SYSDATE);

INSERT INTO notice (id, title, content, writer_name, writer_rank, writer_dept, view_count, is_pinned, has_attachment, reg_date, mod_date)
VALUES (SEQ_NOTICE.NEXTVAL, '[안내] 설 연휴 사내 보안 및 전자기기 점검 지침', '연휴 기간 중 보안 사고 예방을 위해 지침을 준수해 주세요.', '넬슨', '사장', '관리부', 85, 1, 0, TO_TIMESTAMP('2026-01-24 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), SYSDATE);


-- [2] 일반 게시글 98개 등록 (Oracle 전용 CONNECT BY LEVEL 방식)
-- 이 방식은 루프 에러(ORA-06550)를 피할 수 있는 가장 확실한 방법입니다.
INSERT INTO notice (id, title, content, writer_name, writer_rank, writer_dept, view_count, is_pinned, has_attachment, reg_date, mod_date)
SELECT 
    SEQ_NOTICE.NEXTVAL,
    '공지사항 테스트 게시글 제 ' || LEVEL || '호',
    '이것은 페이징 테스트를 위한 더미 데이터 본문입니다. 번호: ' || LEVEL,
    '넬슨', '사장', '관리부', MOD(LEVEL, 50), 0,
    CASE WHEN MOD(LEVEL, 3) = 0 THEN 1 ELSE 0 END,
    SYSDATE - (LEVEL / 10), SYSDATE
FROM DUAL 
CONNECT BY LEVEL <= 98;


INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (1, null, 5, 2, 54, 6, '재직', '2023-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (2, 1, 5, 2, 6, 6, '재직', '2023-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (50, 80, 2, 2, 5, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (52, 81, 2, 2, 5, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (55, 82, 2, 2, 4, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (58, 83, 2, 2, 4, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (60, 50, 3, 2, 3, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (62, 51, 3, 2, 3, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (65, 52, 1, 2, 2, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (68, 53, 1, 2, 2, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (70, 54, 1, 2, 1, 6, '재직', '2024-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (72, 55, 2, 2, 5, 6, '재직', '2025-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (75, 56, 2, 2, 4, 6, '재직', '2025-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (78, 57, 3, 2, 3, 6, '재직', '2025-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (80, 58, 4, 2, 2, 6, '재직', '2025-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (81, 59, 6, 2, 1, 6, '재직', '2025-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (81, 60, 1, 2, 1, 6, '재직', '2026-01-01');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (81, 61, 1, 2, 55, 6, '퇴사', '2025-12-31');

INSERT INTO appointment (member_id, approver_id, pre_dept, current_dept, pre_rank, current_rank, work_status, appointment_date)
VALUES (81, 62, 5, 2, 6, 6, '휴직', '2025-11-11');

COMMIT;