

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


-- Members 테이블 초기 데이터 (5명)



-- 1. 법인장 (임원)
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
             '900101-1234567',                   -- resident_number
             NULL,                               -- profile_photo_link
             'name@yeoun.com',                   -- email
             '010-1234-5678',                    -- phone_number
             '$2a$10$YeG2k7GP9WJ4cRaF47JJyej6PfbNACuay.UphFmACoEiu0Q1hwk8e', -- password
             1,                                  -- department
             55,                                 -- position
             0,                                  -- bank
             '123-456-789012',                   -- account_number
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

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (0, '임원', '총괄', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (1, '인사', '인사', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (2, '구매', '구매', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (3, '영업', '영업', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (4, '재무회계', '재무', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (5, '정보시스템', 'IT', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (6, '경영', '경영', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (7, '안전', '안전', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (8, '법무', '법무', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (100, '공장장', '생산', NULL, NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (101, '프레스', '생산', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (102, '사출', '생산', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (103, '도장', '생산', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (104, '조립', '생산', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (105, '품질', '품질', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (106, '금형', '생산', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (107, '생산관리', '생산관리', '100', NULL, 1);

INSERT INTO departments (id, department_name, category, parent_department, sub_department, is_active)
VALUES (108, '창고', '물류', NULL, NULL, 1);

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
