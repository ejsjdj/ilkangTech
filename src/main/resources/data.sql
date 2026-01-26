
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

INSERT INTO members (
    id,
    name,
    employee_number,
    gender,
    hire_date,
    resident_number,
    profile_photo_link,
    email,
    phone_number,
    password,
    department,
    position,
    bank,
    account_number,
    account_picture_link,
    last_login,
    updated_at
) VALUES (
             1,                                  -- id
             '이순신',                           -- name
             '26-00000',                           -- employee_number
             1,                                  -- gender
             TO_DATE('2026-01-15', 'YYYY-MM-DD'),-- hire_date (26/01/15 기준)
             '900102-1234561',                   -- resident_number
             NULL,                               -- profile_photo_link
             'navma@yeoun.com',                   -- email
             '010-1234-5672',                    -- phone_number
             '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', -- password
             1,                                  -- department
             55,                                 -- position
             0,                                  -- bank
             '122-456-789013',                   -- account_number
             NULL,                               -- account_picture_link
             TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), -- last_login
             TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9')  -- updated_at
         );

INSERT INTO members (
    id,
    name,
    employee_number,
    gender,
    hire_date,
    resident_number,
    profile_photo_link,
    email,
    phone_number,
    password,
    department,
    position,
    bank,
    account_number,
    account_picture_link,
    last_login,
    updated_at
) VALUES (
             2,                                  -- id
             '넬슨',                           -- name
             '26-00001',                           -- employee_number
             1,                                  -- gender
             TO_DATE('2026-01-15', 'YYYY-MM-DD'),-- hire_date (26/01/15 기준)
             '900102-1234567',                   -- resident_number
             NULL,                               -- profile_photo_link
             'nama@yeoun.com',                   -- email
             '010-1234-5679',                    -- phone_number
             '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', -- password
             1,                                  -- department
             55,                                 -- position
             0,                                  -- bank
             '123-456-789013',                   -- account_number
             NULL,                               -- account_picture_link
             TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), -- last_login
             TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9')  -- updated_at
         );

INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (11, '직원11', '26-00010', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000011', NULL, 'user11@example.com', '010-2000-0011', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000011', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (12, '직원12', '26-00011', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000012', NULL, 'user12@example.com', '010-2000-0012', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000012', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (13, '직원13', '26-00012', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000013', NULL, 'user13@example.com', '010-2000-0013', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000013', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (14, '직원14', '26-00013', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000014', NULL, 'user14@example.com', '010-2000-0014', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000014', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (15, '직원15', '26-00014', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000015', NULL, 'user15@example.com', '010-2000-0015', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000015', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (16, '직원16', '26-00015', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000016', NULL, 'user16@example.com', '010-2000-0016', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000016', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (17, '직원17', '26-00016', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000017', NULL, 'user17@example.com', '010-2000-0017', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000017', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (18, '직원18', '26-00017', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000018', NULL, 'user18@example.com', '010-2000-0018', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000018', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (19, '직원19', '26-00018', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000019', NULL, 'user19@example.com', '010-2000-0019', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000019', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (20, '직원20', '26-00019', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000020', NULL, 'user20@example.com', '010-2000-0020', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000020', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (21, '직원21', '26-00020', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000021', NULL, 'user21@example.com', '010-2000-0021', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000021', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (22, '직원22', '26-00021', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000022', NULL, 'user22@example.com', '010-2000-0022', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000022', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (23, '직원23', '26-00022', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000023', NULL, 'user23@example.com', '010-2000-0023', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000023', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (24, '직원24', '26-00023', 2, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000024', NULL, 'user24@example.com', '010-2000-0024', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000024', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));
INSERT INTO members (id, name, employee_number, gender, hire_date, resident_number, profile_photo_link, email, phone_number, password, department, position, bank, account_number, account_picture_link, last_login, updated_at) VALUES (25, '직원25', '26-00024', 1, TO_DATE('2026-01-15', 'YYYY-MM-DD'), '900101-0000025', NULL, 'user25@example.com', '010-2000-0025', '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', 5, 6, 4, '001-000-000025', NULL, TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'), TO_TIMESTAMP('26/01/15 17:33:39.629460100', 'RR/MM/DD HH24:MI:SS.FF9'));

COMMIT;
INSERT INTO member_role(id, member_id, member_role_id)
VALUES (member_role_seq.NEXTVAL, 1, 101);
INSERT INTO member_role(id, member_id, member_role_id)
VALUES (member_role_seq.NEXTVAL, 2, 101);
INSERT INTO member_role(id, member_id, member_role_id)
VALUES (member_role_seq.NEXTVAL, 2, 51);
------------------------- 여기서 부터 멤버 참조에 필요한 데이터 ---------------------------------------------------------------------------------
-- 시중은행
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (4, '004', '국민은행', 'Kookmin Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (20, '020', '우리은행', 'Woori Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (88, '088', '신한은행', 'Shinhan Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (81, '081', '하나은행', 'Hana Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (11, '011', '농협은행', 'NH Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (23, '023', 'SC제일은행', 'SC First Bank', '시중은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (27, '027', '시티은행', 'Citibank Korea', '시중은행', 1);
-- 인터넷 전문은행
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (90, '090', '카카오뱅크', 'Kakao Bank', '인터넷은행', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (92, '092', '토스뱅크', 'Toss Bank', '인터넷은행', 1);
-- 기타 금융기관
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (48, '048', '신용협동조합', 'Credit Union', '기타금융', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (45, '045', '새마을금고', 'Saemaul Geumgo', '기타금융', 1);
INSERT INTO banks (id, bank_code, bank_name, english_name, category, is_active)
VALUES (2, '002', '산업은행', 'Korea Development Bank', '기타금융', 1);
COMMIT;
-- Departments 테이블 초기 데이터
-- 부서 (0 임원 1 인사 2 구매 3 영업 4 재무회계 5 정보시스템 6 경영 7 안전 8 법무 100 공장장 101 프레스 102 사출 103 도장 104 조립 105 품질 106 금형 107 생산관리 108)
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (0, '대표', null, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (1, '관리부', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (2, '인사', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (3, '구매', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (4, '영업', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (5, '재무회계', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (6, '정보시스템', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (7, '경영', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (8, '안전', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (9, '법무', 1, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (100, '생산부', 0, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (101, '프레스', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (102, '사출', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (103, '도장', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (104, '조립', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (105, '품질', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (106, '금형', 100, 1);
INSERT INTO departments (id, department_name, parent_department, is_active)
VALUES (107, '생산관리', 100, 1);

COMMIT;

-- Genders 테이블 초기 데이터
-- 성별 (1 남자 2 여자)
INSERT INTO genders (id, gender)
VALUES (1, '남자');
INSERT INTO genders (id, gender)
VALUES (2, '여자');
COMMIT;
-- Positions 테이블 초기 데이터
-- 직급 (1 부장 2 차장 3 과장 4 대리 5 주임 6 사원 51 이사 52 상무이사 53 전무이사 54 부사장 55 사장 56 대표이사)
--     (101 기능공 102 기능사 103 선임기능사 104 기능장 105 수석기능장)
-- 일반직
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (1, '부장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (2, '차장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (3, '과장', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (4, '대리', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (5, '주임', '일반직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (6, '사원', '일반직', 1);
-- 임원직
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (51, '이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (52, '상무이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (53, '전무이사', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (54, '부사장', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (55, '사장', '임원직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (56, '대표이사', '임원직', 1);
-- 기술직
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (101, '기능공', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (102, '기능사', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (103, '선임기능사', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (104, '기능장', '기술직', 1);
INSERT INTO positions (id, position_name, position_type, is_active)
VALUES (105, '수석기능장', '기술직', 1);
COMMIT;

-- [1] 1~50번 사원의 '회사(COMPANY)' 일정 일괄 등록
INSERT INTO IK_SCHEDULE (
    schedule_id,
    member_id,
    schedule_title,    -- 수정됨 (title -> schedule_title)
    schedule_memo,     -- 수정됨 (content -> schedule_memo)
    schedule_type,     -- 수정됨 (type -> schedule_type)
    start_date,
    end_date,
    reg_date
)
SELECT
    SEQ_SCHEDULE.NEXTVAL,
    LEVEL,
    '1월 전사 정기 회의',
    '2026년 상반기 목표 공유 및 부서별 현황 발표',
    'COMPANY',
    TO_DATE('2026-01-20 09:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_DATE('2026-01-20 11:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    SYSDATE
FROM DUAL
    CONNECT BY LEVEL <= 2;

-- [2] 1~50번 사원의 '개인(PERSONAL)' 일정 일괄 등록
INSERT INTO IK_SCHEDULE (
    schedule_id,
    member_id,
    schedule_title,    -- 수정됨
    schedule_memo,     -- 수정됨
    schedule_type,     -- 수정됨
    start_date,
    end_date,
    reg_date
)
SELECT
    SEQ_SCHEDULE.NEXTVAL,
    LEVEL,
    '개인 연차/반차 (사원 ' || LEVEL || ')',
    '개인 사정으로 인한 연차 사용',
    'PERSONAL',
    TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS') + MOD(LEVEL, 5),
    TO_DATE('2026-01-21 18:00:00', 'YYYY-MM-DD HH24:MI:SS') + MOD(LEVEL, 5),
    SYSDATE
FROM DUAL
    CONNECT BY LEVEL <= 2;

-- DraftDocument 더미 데이터 10개 생성
INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (1, '휴가신청서', '연차 신청합니다.', '개인 사정으로 인한 연차 신청', 'download/illkang/vacation_request.pdf',TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-06-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'),  '승인');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file,draft_start_date, draft_end_date, draft_status)
VALUES (1, '지출결의서', '비품 구매 건', '사무용품(A4용지 등) 구매', 'download/illkang/expense_report.docx', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-06-17 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '승인');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (1, '기안서', '신규 프로젝트 제안', 'AI 기반 서비스 도입 제안서', 'download/illkang/proposal_v1.pdf', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-03-09 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '반려');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (1, '휴가신청서', '오후 반차 신청', '병원 방문', 'download/illkang/sick.pdf', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-08-11 14:00:00', 'YYYY-MM-DD HH24:MI:SS'),  '승인');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (2, '출장신청서', '서울 본사 출장', '업무 협의 및 미팅', 'download/illkang/business_trip.pdf', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-05-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '승인');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (2, '지출결의서', '회식비 청구', '개발팀 정기 회식', 'download/illkang/receipt_01.jpg', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-05-27 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '반려');

INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
VALUES (2, '기안서', '노후 장비 교체', '개발용 모니터 교체 요청', 'download/illkang/hardware_list.xlsx', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-06-08 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '승인');

-- INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
-- VALUES (3, '휴가신청서', '여름 휴가 신청', '가족 여행', 'download/illkang/summer_vacation.pdf', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-06-18 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '승인');

-- INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
-- VALUES (3, '지출결의서', '도서 구입비', '자바 최적화 가이드 등 3권', 'download/illkang/croma/crime/candy/book_receipt.pdf', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-04-01 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), '승인');
--
-- INSERT INTO draft_document (common_id, draft_type, draft_title, draft_content, draft_file, draft_start_date, draft_end_date, draft_status)
-- VALUES (4, '기안서', '워크샵 기안', '상반기 팀 워크샵 장소 선정', 'download/illkang/vincent/workshop_plan.pptx', TO_DATE('2026-01-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2026-07-22 14:00:00', 'YYYY-MM-DD HH24:MI:SS'),  '승인');

-- Draft_Approval_line 더미 데이터
INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('PTO', 11, 1);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('PTO', 12, 2);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('BUY', 20, 1);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('BUY', 21, 2);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('BUY', 22, 3);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('HDF', 15, 1);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('HDF', 16, 2);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('HDF', 17, 3);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('APP', 1, 1);

INSERT INTO draft_approval_line (draft_type, common_id, sequence)
VALUES ('APP', 2, 2);

-- LEAVE_STATUS 더미 데이터

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (1, 15, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (2, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (11, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (12, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (13, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (14, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (15, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (16, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (17, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (18, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (19, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (20, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (21, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (22, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (23, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (24, 16, 2, 14);

INSERT INTO leave_status (common_id, total_leave, used_leave, remain_leave)
VALUES (25, 16, 2, 14);

-- 반영
COMMIT;