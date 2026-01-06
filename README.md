📋 프로젝트 소개
일광건조 ERP는 기업의 핵심 비즈니스 프로세스를 통합 관리하는 전사적 자원 관리 시스템입니다. 고객 관리, 주문 처리, 재고 관리, 발주 관리 등 기업 운영에 필요한 핵심 기능을 제공합니다.

주요 기능
👥 인사 관리: 사원 정보 등록, 조회, 수정, 삭제

📦 주문 관리: 주문 등록, 상태 추적, 이력 관리

📊 재고 관리: 실시간 재고 현황 파악 및 재고 조정

🛒 발주 관리: 발주 요청 및 승인 프로세스

🔐 인증/인가: JWT 기반 보안 시스템

📈 대시보드: 주요 지표 시각화 및 모니터링

🛠 기술 스택
Backend
Java
Spring Boot
JPA
Oracle

언어: Java 21

프레임워크: Spring Boot 3.x

ORM: JPA (Hibernate)

인증: JWT (JSON Web Token)

데이터베이스: Oracle / H2 (개발/테스트)

템플릿 엔진: Thymeleaf

빌드 도구: Maven / Gradle

주요 라이브러리:

Spring Data JPA

Spring Security

Validation API

Lombok

Frontend
React
Bootstrap
Axios

라이브러리: React 18.x

CSS 프레임워크: Bootstrap 5

상태관리: Context API / Redux Toolkit

HTTP 클라이언트: Axios

라우팅: React Router DOM

빌드 도구: Vite / Create React App

Git

버전관리: Git / GitHub

배포: AWS


🚀 시작하기
사전 요구사항
Java 21 이상

Node.js 18 이상

MySQL 8.0 이상 (또는 H2 사용)

Maven 3.8+ 또는 Gradle 8+

Backend 설치 및 실행
레포지토리 클론

bash
git clone https://github.com/your-username/ilgwang-erp.git
cd ilgwang-erp/backend
데이터베이스 설정

sql
CREATE DATABASE ilgwang_erp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
application.yml 설정

text
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ilgwang_erp
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-secret-key-here
  expiration: 86400000
애플리케이션 실행

bash
# Maven 사용 시
./mvnw spring-boot:run

# Gradle 사용 시
./gradlew bootRun
서버가 http://localhost:8080에서 실행됩니다.

Frontend 설치 및 실행
프론트엔드 디렉토리 이동

bash
cd ../frontend
의존성 설치

bash
npm install
환경변수 설정

bash
# .env 파일 생성
VITE_API_BASE_URL=http://localhost:8080/api
개발 서버 실행

bash
npm run dev
프론트엔드가 http://localhost:5173에서 실행됩니다.

🔑 API 문서
애플리케이션 실행 후 Swagger UI를 통해 API 문서를 확인할 수 있습니다.

text
http://localhost:8080/swagger-ui/index.html
주요 API 엔드포인트
인증
POST /api/auth/login - 로그인

POST /api/auth/register - 회원가입

POST /api/auth/refresh - 토큰 갱신

고객 관리
GET /api/customers - 고객 목록 조회

GET /api/customers/{id} - 고객 상세 조회

POST /api/customers - 고객 등록

PUT /api/customers/{id} - 고객 정보 수정

DELETE /api/customers/{id} - 고객 삭제

주문 관리
GET /api/orders - 주문 목록 조회

GET /api/orders/{id} - 주문 상세 조회

POST /api/orders - 주문 등록

PUT /api/orders/{id} - 주문 수정

PATCH /api/orders/{id}/status - 주문 상태 변경

🔐 인증 및 보안
JWT 토큰 기반 인증
Access Token: 1시간 유효

Refresh Token: 7일 유효

Authorization Header: Bearer {token}

보안 기능
BCrypt 비밀번호 암호화

CORS 설정

XSS 방지

CSRF 보호

SQL Injection 방지 (Prepared Statement)

🧪 테스트
Backend 테스트
bash
# 전체 테스트 실행
./mvnw test

# 특정 테스트 클래스 실행
./mvnw test -Dtest=CustomerServiceTest
Frontend 테스트
bash
# 단위 테스트
npm run test

# 커버리지 확인
npm run test:coverage
📦 배포
Docker를 이용한 배포
Docker 이미지 빌드

bash
# Backend
docker build -t ilgwang-erp-backend ./backend

# Frontend
docker build -t ilgwang-erp-frontend ./frontend
Docker Compose 실행

bash
docker-compose up -d
프로덕션 빌드
Backend

bash
./mvnw clean package -DskipTests
java -jar target/ilgwang-erp-0.0.1-SNAPSHOT.jar
Frontend

bash
npm run build
🤝 기여 방법
Fork the Project

Create your Feature Branch (git checkout -b feature/AmazingFeature)

Commit your Changes (git commit -m 'Add some AmazingFeature')

Push to the Branch (git push origin feature/AmazingFeature)

Open a Pull Request

📝 커밋 컨벤션
text
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅, 세미콜론 누락 등
refactor: 코드 리팩토링
test: 테스트 코드 추가
chore: 빌드 업무 수정, 패키지 매니저 수정
📊 ERD (Entity Relationship Diagram)
text
┌─────────────────┐         ┌─────────────────┐
│      User       │         │    Customer     │
├─────────────────┤         ├─────────────────┤
│ id (PK)        │         │ id (PK)        │
│ username       │         │ name           │
│ password       │         │ email          │
│ email          │         │ phone          │
│ role           │         │ address        │
│ created_at     │         │ created_at     │
└─────────────────┘         └────────┬────────┘
                                     │
                                     │ 1:N
                                     │
                            ┌────────┴────────┐
                            │     Order       │
                            ├─────────────────┤
                            │ id (PK)        │
                            │ customer_id(FK)│
                            │ order_date     │
                            │ status         │
                            │ total_amount   │
                            └────────┬────────┘
                                     │
                                     │ 1:N
                                     │
                            ┌────────┴────────┐
                            │  OrderItem      │
                            ├─────────────────┤
                            │ id (PK)        │
                            │ order_id (FK)  │
                            │ product_id(FK) │
                            │ quantity       │
                            │ price          │
                            └────────┬────────┘
                                     │
                                     │ N:1
                                     │
                            ┌────────┴────────┐
                            │    Product      │
                            ├─────────────────┤
                            │ id (PK)        │
                            │ name           │
                            │ description    │
                            │ price          │
                            │ stock_quantity │
                            └─────────────────┘
🎯 개발 로드맵
Phase 1: 기본 기능 구현 (완료)
 프로젝트 초기 설정

 데이터베이스 스키마 설계

 인증/인가 시스템 구축

 CRUD API 개발

Phase 2: 핵심 비즈니스 로직 (진행 중)
 주문 처리 프로세스

 재고 관리 시스템

 발주 관리 시스템

 대시보드 구현

Phase 3: 고도화 (예정)
 실시간 알림 기능

 보고서 생성 기능

 데이터 분석 및 통계

 모바일 반응형 최적화

🐛 알려진 이슈
현재 알려진 이슈가 없습니다. 버그를 발견하시면 Issues에 등록해주세요.

📄 라이선스
이 프로젝트는 MIT 라이선스 하에 있습니다. 자세한 내용은 LICENSE 파일을 참조하세요.

👥 개발팀
프로젝트 리더: [이름]

백엔드 개발: [이름]

프론트엔드 개발: [이름]

데이터베이스 설계: [이름]

📞 문의
프로젝트에 대한 문의사항이 있으시면 다음으로 연락주세요:

Email: your.email@example.com

GitHub Issues: 프로젝트 이슈
