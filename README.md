# Spring Blog Practice

Spring Boot와 Spring Data JPA를 활용하여 구현한 블로그 웹 프로젝트입니다. 게시글 CRUD, 회원 로그인, 작성자 권한 검사, 좋아요 기능 등을 구현하며 웹 애플리케이션의 기본적인 구조와 데이터 처리 과정을 학습했습니다.

## 📌 프로젝트 소개

- **프로젝트명:** Spring Blog Practice
- **목적:** Spring Boot 기반 웹 애플리케이션 개발 학습
- **개발 환경:** IntelliJ IDEA
- **주요 기능:** 회원 관리, 게시글 관리, 좋아요 기능

## 🛠️ 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Template Engine | Mustache |
| Frontend | HTML, CSS, JavaScript, Bootstrap |
| Build Tool | Gradle |
| Version Control | Git, GitHub |

## ✨ 주요 기능

### 1. 회원 관리
- 회원가입
- 로그인 및 로그아웃
- 세션 기반 로그인 상태 관리
- 회원 정보 수정

### 2. 게시글 관리
- 게시글 목록 조회
- 게시글 상세 조회
- 게시글 작성
- 게시글 수정 및 삭제
- 작성자 본인 여부 확인을 통한 수정·삭제 권한 검사

### 3. 좋아요 기능
- 게시글별 좋아요 수 표시
- 비동기 요청을 통한 좋아요 수 증가
- JavaScript Fetch API를 활용한 서버 통신

### 4. 예외 처리 및 요청 제어
- 사용자 정의 예외 처리
- 공통 예외 처리 구조 학습
- 인터셉터를 활용한 요청 제어 학습

## 🏗️ 프로젝트 구조

```text
src/main/java/com/tenco/spring_blog_practice/
├── _core/
│   ├── error/          # 예외 및 공통 예외 처리
│   ├── interceptor/    # 요청 인터셉터
│   └── util/           # 공통 유틸리티
├── board/
│   ├── Board.java
│   ├── BoardController.java
│   ├── BoardApiController.java
│   ├── BoardService.java
│   ├── BoardJpaRepository.java
│   └── BoardRequest.java
└── user/
    ├── User.java
    ├── UserController.java
    ├── UserService.java
    ├── UserJpaRepository.java
    └── UserRequest.java
```

> 실제 프로젝트의 패키지와 파일 구성에 따라 구조는 달라질 수 있습니다.

## 🔄 기본 처리 흐름

```text
사용자 요청
    ↓
Controller
    ↓
Service
    ↓
JpaRepository
    ↓
Database
```

- **Controller:** HTTP 요청을 받고 응답 및 화면 이동을 처리합니다.
- **Service:** 비즈니스 로직과 권한 검사 등을 처리합니다.
- **JpaRepository:** 데이터베이스 조회 및 저장·수정·삭제를 처리합니다.
- **Entity:** 데이터베이스 테이블과 객체를 매핑합니다.
- **Mustache:** 서버에서 전달받은 데이터를 화면에 표시합니다.

## 🚀 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/DevDash-boot/spring_blog_practice.git
cd spring_blog_practice
```

### 2. 데이터베이스 설정

MySQL에 사용할 데이터베이스를 생성하고 프로젝트의 환경 설정에 맞게 연결 정보를 지정합니다.

예시:

```sql
CREATE DATABASE myblog2;
```

DB 주소, 사용자 이름, 비밀번호는 `application.yaml` 및 `.env` 설정에 맞게 변경합니다.

### 3. 환경 변수 설정

`.env` 파일을 사용하는 경우 다음과 같이 설정합니다.

```dotenv
DB_URL=jdbc:mysql://localhost:3306/myblog2?serverTimezone=Asia/Seoul
DB_USERNAME=root
DB_PASSWORD=your_password
```

실제 데이터베이스 비밀번호가 포함된 `.env` 파일은 GitHub에 업로드하지 않습니다.

### 4. 애플리케이션 실행

IntelliJ IDEA에서 Spring Boot 메인 클래스를 실행하거나 터미널에서 다음 명령어를 입력합니다.

```bash
./gradlew bootRun
```

Windows 환경에서는 다음 명령어를 사용할 수 있습니다.

```powershell
.\gradlew.bat bootRun
```

서버가 실행되면 브라우저에서 설정된 포트로 접속합니다. 기본 포트를 사용하는 경우 `http://localhost:8080`입니다.

## 📚 학습 내용

- Spring MVC의 Controller와 HTTP 요청 처리
- 계층형 아키텍처(Controller-Service-Repository)
- JPA Entity와 테이블 매핑
- Spring Data JPA를 이용한 CRUD
- 연관관계 매핑과 `@ManyToOne`
- 세션을 활용한 로그인 상태 관리
- 사용자 정의 예외와 공통 예외 처리
- 트랜잭션과 영속성 컨텍스트
- Fetch API를 이용한 비동기 통신
- Git과 GitHub를 이용한 버전 관리

## ⚠️ 참고 사항

- 본 프로젝트는 학습 및 실습 목적으로 제작되었습니다.
- 비밀번호 암호화, 좋아요 중복 방지 및 취소 기능 등은 별도 구현이 필요할 수 있습니다.
- 실제 서비스에 적용할 경우 인증·인가, 입력값 검증, 보안 설정 등을 추가로 점검해야 합니다.

## 👨‍💻 Repository

[GitHub - spring_blog_practice](https://github.com/DevDash-boot/spring_blog_practice)
