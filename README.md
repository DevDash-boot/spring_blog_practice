# Spring Blog Practice

Spring Boot와 Spring Data JPA로 만든 개발자 블로그(DevBlog) 웹 프로젝트입니다. 게시글·댓글·좋아요 같은 기본 기능에 더해 이메일·SMS 인증, 소셜 로그인, 포트원 결제를 통한 포인트 충전, 포인트로 열람하는 **유료 게시글**까지 구현하며 웹 애플리케이션의 전체 흐름을 학습했습니다.

## 📌 프로젝트 소개

- **프로젝트명:** Spring Blog Practice (DevBlog)
- **목적:** Spring Boot 기반 웹 애플리케이션 개발 학습
- **개발 환경:** IntelliJ IDEA, JDK 21
- **주요 기능:** 회원/인증, 게시글, 유료 게시글, 포인트 결제, 좋아요, 댓글, 관리자

## 🛠️ 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 (Spring MVC) |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL (local, prod) / H2 in-memory (dev) |
| Template Engine | Mustache |
| Frontend | HTML, CSS, JavaScript, Bootstrap 5, Font Awesome |
| 인증/보안 | 세션 로그인, BCrypt(spring-security-crypto), 인터셉터 |
| 외부 연동 | Gmail SMTP, SOLAPI(SMS), Kakao·Google·Naver OAuth, PortOne V2(결제) |
| Build Tool | Gradle |
| Version Control | Git, GitHub |

## ✨ 주요 기능

### 1. 회원 관리
- 회원가입 (**이메일 인증번호 확인 후 가입**, 프로필 이미지 선택 업로드)
- 로그인 / 로그아웃, 세션 기반 로그인 상태 관리, 아이디 저장
- **소셜 로그인:** 카카오, 구글, 네이버 (최초 로그인 시 자동 가입)
- 마이페이지: 회원 정보·보유 포인트 확인, 비밀번호/프로필 이미지 수정·삭제
- 역할(Role) 기반 권한: `USER`, `ADMIN`

### 2. 게시글 관리
- 게시글 목록 조회 (페이징, 제목·내용 키워드 검색)
- 게시글 상세 조회, 작성, 수정, 삭제
- 작성자 본인 여부 확인을 통한 수정·삭제 권한 검사
- 게시글 삭제 시 댓글·좋아요·구매 내역을 함께 정리

### 3. 유료 게시글 & 포인트
- 작성 시 **유료 게시글**로 등록 가능 (열람 가격 `500P`, `Board.PREMIUM_PRICE`)
- 유료 게시글은 **작성자 본인 또는 구매자**만 본문을 볼 수 있고, 그 외에는 구매 안내 화면 표시
    - 서버에서 본문을 내려주지 않으므로 개발자 도구로 우회할 수 없음
- 포인트 차감과 구매 내역 저장을 **하나의 트랜잭션**으로 처리, 중복 구매 방지(유니크 제약)
- 포인트 부족 시 안내 후 마이페이지로 이동
- **포인트 충전:** PortOne 결제창 → 서버가 PortOne API로 결제 상태·금액을 직접 검증한 뒤 충전
  (결제 건 본인 확인, 이미 처리된 결제 거부, 금액 위·변조 확인)

### 4. 좋아요 기능
- 게시글별 좋아요 수와 내가 누른 상태 표시
- Fetch API 비동기 요청으로 좋아요 / 취소 (사용자당 게시글 1회)

### 5. 댓글 기능
- 게시글 상세 화면에서 댓글 목록 조회·작성
- 댓글 수정 및 삭제 (작성자 본인만 가능)

### 6. 인증·보안 보조 기능
- **이메일 인증:** 6자리 인증번호, 유효시간 1분, 재발송 30초 제한, 시도 5회 제한
- **SMS 인증:** SOLAPI로 인증번호 문자 발송, 인증 완료 시 사용자 휴대폰 인증 처리
- 프로필 이미지 형식 제한(JPG, PNG, GIF, WEBP), 업로드 폴더 `~/blog_uploads`
- 특정 IP 차단 인터셉터

### 7. 예외 처리 및 요청 제어
- 사용자 정의 예외(400, 401, 403, 404, 500)와 `@ControllerAdvice` 공통 처리
- 화면 요청은 에러 페이지로, API 요청(`/api/**`)은 **JSON 응답**으로 에러 반환
- 인터셉터로 로그인 여부·관리자 권한 검사 (`LoginInterceptor`, `AdminInterceptor`)

### 8. 관리자
- 관리자 전용 대시보드 (`/admin/dashboard`)

## 🏗️ 프로젝트 구조

```text
src/main/java/com/tenco/spring_blog_practice/
├── _core/
│   ├── config/         # WebMvcConfig (인터셉터 등록, 업로드 경로, PasswordEncoder)
│   ├── error/          # 사용자 정의 예외, GlobalException
│   ├── interceptor/    # IpBlock, SessionUserModel, Login, Admin 인터셉터
│   └── util/           # Define, FileUtil, MailUtil, MyDataUtil
├── board/              # 게시글, 좋아요 (Board, BoardLike, Controller, Service ...)
├── reply/              # 댓글
├── user/               # 회원, 역할, 이메일 인증, 마이페이지, 포인트
├── oauth/              # 카카오·구글·네이버 로그인 (ApiClient + LoginService)
├── sms/                # SOLAPI 문자 인증
├── purchase/           # 유료 게시글 구매 내역 (Purchase, PurchaseService)
├── Payment/            # 포인트 충전 결제 (PortOne 연동)
└── admin/              # 관리자 대시보드

src/main/resources/
├── templates/          # Mustache 화면 (board, user, admin, err, layout)
├── static/             # CSS, 이미지
├── db/data.sql         # 초기 샘플 데이터
└── application*.yaml   # 공통 / local / dev / prod 설정
```

## 🔄 기본 처리 흐름

```text
사용자 요청 → Interceptor → Controller → Service → JpaRepository → Database
```

- **Interceptor:** 차단 IP, 로그인·관리자 여부를 컨트롤러 이전에 검사합니다.
- **Controller:** HTTP 요청을 받고 응답 및 화면 이동을 처리합니다.
- **Service:** 비즈니스 로직, 권한 검사, 트랜잭션을 처리합니다.
- **JpaRepository:** 데이터베이스 조회 및 저장·수정·삭제를 처리합니다.
- **Entity:** 데이터베이스 테이블과 객체를 매핑합니다.
- **Mustache:** 서버에서 전달받은 데이터를 화면에 표시합니다.

### 유료 게시글 구매 흐름

```text
유료 게시글 상세 조회
    ↓ (작성자도 구매자도 아님)
구매 안내 화면 (본문은 서버에서 내려주지 않음)
    ↓ 구매하기 (POST /board/{id}/purchase)
PurchaseService.purchase()   ← 하나의 트랜잭션
    ├─ 유료 글 / 본인 글 아님 / 미구매 여부 검증
    ├─ 구매자 포인트 차감 (부족하면 NotEnoughPointException)
    └─ 구매 내역(Purchase) 저장
    ↓
게시글 상세 화면으로 이동 → 본문 열람 가능
```

### 포인트 충전 흐름

```text
충전 금액 선택 (1,000 ~ 100,000P)
    ↓ POST /api/payment/prepare   → 결제 건(READY) 생성, paymentId 발급
PortOne 결제창
    ↓ POST /api/payment/complete  → 서버가 PortOne API로 결제 조회
검증 (본인 결제 · 중복 처리 · PAID 상태 · 금액 일치)
    ↓
포인트 충전 + 결제 건 PAID 처리
```

## 🌐 주요 URL

| 구분 | Method | URL | 설명 |
|---|---|---|---|
| 게시글 | GET | `/`, `/board/list` | 목록 (`page`, `size`, `keyword`) |
| | GET | `/board/{id}` | 상세 |
| | GET/POST | `/board/save` | 작성 |
| | GET/POST | `/board/{id}/update` | 수정 |
| | POST | `/board/{id}/delete` | 삭제 |
| | POST | `/board/{id}/purchase` | 유료 게시글 구매 |
| | POST | `/api/boards/{id}/like` | 좋아요 토글 (JSON) |
| 댓글 | POST | `/reply/save`, `/reply/{id}/update`, `/reply/{id}/delete` | 댓글 작성·수정·삭제 |
| 회원 | GET/POST | `/join`, `/login` | 가입, 로그인 |
| | GET | `/kakao-login`, `/google-login`, `/naver-login` | 소셜 로그인 |
| | GET | `/user/detail`, `/user/update` | 마이페이지, 정보 수정 |
| | GET | `/user/point/charge` | 포인트 충전 화면 |
| 인증 | POST | `/api/email/send`, `/api/email/verify` | 이메일 인증 |
| | POST | `/api/sms/send`, `/api/sms/verify` | SMS 인증 |
| 결제 | POST | `/api/payment/prepare`, `/api/payment/complete` | 결제 준비·검증 |
| 관리자 | GET | `/admin/dashboard` | 관리자 전용 |

## 🚀 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/DevDash-boot/spring_blog_practice.git
cd spring_blog_practice
```

### 2. 데이터베이스 설정

기본 프로필(`local`)은 MySQL을 사용합니다. 데이터베이스를 먼저 생성합니다.

```sql
CREATE DATABASE myblog2;
```

> DB 없이 바로 실행해 보려면 H2 메모리 DB를 쓰는 `dev` 프로필로 실행할 수 있습니다.
> `./gradlew bootRun --args='--spring.profiles.active=dev'`
> (H2 콘솔: `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:blogdb`)

### 3. 환경 변수 설정

프로젝트 루트의 `.env.example`을 복사해 `.env`를 만들고 값을 채웁니다.

```bash
cp .env.example .env
```

| 변수 | 용도 |
|---|---|
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | MySQL 접속 정보 (`jdbc:mysql://localhost:3306/myblog2?serverTimezone=Asia/Seoul`) |
| `KAKAO_*`, `GOOGLE_*`, `NAVER_*` | 소셜 로그인 `CLIENT_ID`, `CLIENT_SECRET`, `REDIRECT_URI` |
| `MAIL_USERNAME`, `MAIL_PASSWORD` | Gmail 주소와 **앱 비밀번호**(16자리, 구글 로그인 비밀번호 아님) |
| `SOLAPI_API_KEY`, `SOLAPI_API_SECRET`, `SOLAPI_SENDER` | SMS 인증 발송 |
| `PORTONE_STORE_ID`, `PORTONE_CHANNEL_KEY`, `PORTONE_API_SECRET` | 포트원 V2 결제 |

- 소셜 로그인·메일·SMS 값은 기본값이 없으므로 **변수 자체는 정의되어 있어야** 서버가 시작됩니다. 사용하지 않는 기능은 빈 값(`KEY=`)으로 두면 됩니다. (해당 기능만 동작하지 않습니다.)
- 포트원 값은 비어 있어도 서버는 시작되지만 결제는 실패합니다.
- ⚠️ 포트원 API Secret 변수명이 `PROTONE_API_SECRET`에서 **`PORTONE_API_SECRET`으로 변경**되었습니다. 기존 `.env`가 있다면 이름을 수정하세요.
- 비밀번호·API 키가 담긴 `.env`는 GitHub에 올리지 않습니다. (`.gitignore`에 등록됨)

### 4. 애플리케이션 실행

IntelliJ IDEA에서 `SpringBlogPracticeApplication`을 실행하거나 터미널에서 입력합니다.

```bash
./gradlew bootRun
```

Windows 환경에서는 다음 명령어를 사용할 수 있습니다.

```powershell
.\gradlew.bat bootRun
```

서버 실행 후 브라우저에서 접속합니다.

```text
http://localhost:8080
```

### 프로필 요약

| 프로필 | DB | 포트 | 특징 |
|---|---|---|---|
| `local` (기본) | MySQL | 8080 | 시작할 때마다 테이블 재생성(`create`) + `data.sql` 로드 |
| `dev` | H2 메모리 | 8080 | DB 설치 없이 실행, H2 콘솔 사용 가능 |
| `prod` | MySQL | 5000 | `ddl-auto: update`, 샘플 데이터 미적용 |

## 🧪 샘플 데이터 (`db/data.sql`)

`local`/`dev` 프로필로 실행하면 사용자 20명, 게시글 40개(무료 22 / 유료 18), 댓글 80개, 좋아요가 자동으로 들어갑니다. **모든 계정의 비밀번호는 `1234`** 입니다.

| 계정 | 포인트 | 설명 |
|---|---|---|
| `admin` | 10,000P | 관리자(`ADMIN` + `USER`), 무료 글 1개 + 유료 글 1개 작성 |
| `minsu` | 5,000P | 무료 글 1개 + 유료 글 2개 작성 |
| `jihun`, `seoyeon`, `yujin`, `sumin`, `jiwoo`, `taeyang`, `sangho`, `chaewon`, `nari`, `harin` | 500P 이상 | 유료 글 구매 가능 |
| `junho` | 300P | 포인트는 있지만 부족 → 구매 실패 테스트용 |
| `dohyun`, `hyunwoo`, `eunji`, `yuna`, `haneul`, `seungho`, `geonwoo` | 0P | 포인트 없음 |

## 📚 학습 내용

- Spring MVC의 Controller와 HTTP 요청 처리
- 계층형 아키텍처(Controller-Service-Repository)와 인터셉터
- JPA Entity와 테이블 매핑, 연관관계, 유니크 제약
- Spring Data JPA 페이징, JOIN FETCH, 벌크 삭제 쿼리
- 세션 로그인, BCrypt 비밀번호 해싱, 역할 기반 접근 제어
- 사용자 정의 예외와 공통 예외 처리(화면용 / JSON용 분리)
- 트랜잭션과 영속성 컨텍스트, 더티 체킹
- OAuth 2.0 인가 코드 방식 소셜 로그인(카카오·구글·네이버)
- 이메일(SMTP)·SMS 인증번호 발급과 검증
- 외부 결제 연동과 서버 측 결제 검증(금액 위·변조 방지)
- Fetch API를 이용한 비동기 통신
- Git과 GitHub를 이용한 버전 관리

## ⚠️ 참고 사항

- 본 프로젝트는 학습 및 실습 목적으로 제작되었습니다.
- 유료 게시글 구매 시 포인트는 구매자에게서만 차감되며, 작성자에게 정산되지는 않습니다.
- 실제 서비스에 적용할 경우 CSRF 방어, 요청 횟수 제한(SMS·메일), 결제 동시 요청 처리(락), 로그아웃 POST 전환 등 보안 설정을 추가로 점검해야 합니다.

## 🖥️ 실행 화면

<img width="1280" height="816" alt="image" src="https://github.com/user-attachments/assets/6623a8e6-5c57-42c6-a0e9-a2cb9eda3bd3" />

<img width="1280" height="641" alt="image" src="https://github.com/user-attachments/assets/c7bd8f03-2a36-45d6-94fd-1a26a57afec5" />

<img width="947" height="542" alt="image" src="https://github.com/user-attachments/assets/856b8334-933c-402f-8fbb-97409baaf022" />

<img width="997" height="822" alt="image" src="https://github.com/user-attachments/assets/67e67451-5201-46d5-8bb3-33884b288aa5" />

<img width="970" height="613" alt="image" src="https://github.com/user-attachments/assets/054a9415-1e1c-4bff-a55e-e80f9602e9d7" />

<img width="914" height="845" alt="image" src="https://github.com/user-attachments/assets/82721396-410c-44da-a087-11b432e735b1" />

## 👨‍💻 Repository

[GitHub - spring_blog_practice](https://github.com/DevDash-boot/spring_blog_practice)