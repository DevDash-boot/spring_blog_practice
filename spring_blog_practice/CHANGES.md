# 변경 내역 (수업용 spring_blog 기준 점검 + practice 기능 반영)

## 1. 새로 추가한 기능
| 기능 | 위치 |
|---|---|
| 공통 페이징 | `_core/util/PageResponse`, `templates/layout/pagination.mustache` |
| 결제 내역 페이징 + **환불 요청 버튼 연결** | `PaymentService`, `PaymentJpaRepository`, `user/payment-list.mustache` |
| 환불 요청 내역 페이징 | `RefundService`, `RefundRepository`, `refund/list.mustache` |
| 구매 내역 페이징 | `PurchaseService`, `PurchaseJpaRepository`, `user/purchase-list.mustache` |
| 관리자 환불 관리 페이징 + 상태 필터(대기/승인/거절) + 거절 사유 표시 | `AdminController`, `admin/refund-list.mustache` |
| 관리자 대시보드 환불 현황(건수) | `admin/dashboard.mustache` |
| Gemini AI 맞춤법 교정 | `agent/RestAgentController`, `static/js/template.js`, 글쓰기/수정 화면 |
| 마이페이지 휴대폰 인증 UI | `user/detail.mustache`, `sms/*` |

## 2. 오류 수정
- `data.sql` : 결제 상태 `'CANCEL'` → `'CANCELLED'` (enum에 없는 값이라 결제 내역 조회 시 예외 발생)
- 게시글 상세 : 좋아요 수/내가 누른 상태가 항상 0/false로 표시되던 문제 (서비스에서 계산하도록 수정)
- 게시글 삭제 : 구매 내역이 있는 글은 삭제 불가(수업 코드 기준 복구) — 외래키 오류 방지
- 게시글 수정 : 유료 체크박스가 기존 값을 반영하지 않아 수정 시 무료로 바뀌던 문제
- `PurchaseJpaRepository.deleteByBoardId` : `@Modifying` 쿼리의 반환형 `boolean` → `int`
- `GlobalExceptionHandler` : alert 문자열 따옴표 이스케이프(`"\\"` → `"\\'"`)
- 포인트 충전 JS : 실패해도 다음 단계로 넘어가던 흐름, 결제 취소 판정 조건(`!paymentRes && ...`), 버튼 사이 클릭 시 null 오류
- 결제 API : 오류가 HTML로 응답되어 JS `response.json()` 이 깨지던 문제 → JSON 응답
- 결제 내역 화면 : `paymentAt`(→`paidAt`), `statusBadgeClass` 누락, 환불 버튼 없음, 깨진 div 구조
- 관리자 환불 화면 : 행마다 같은 id의 모달이 중복 생성되던 문제, `from-label`/`form-control-platintext` 오타
- `application-local.yaml` : 로그 패키지명, `h2.console.enable` 오타 / 전체 yaml `open-In-view` → `open-in-view`
- 환경변수 오타 `PROTONE_API_SECRET` → `PORTONE_API_SECRET` (기존 이름도 호환 지원)
- 네이버 콜백 `state` 누락 시 400 오류 → 안내 메시지 처리
- 문자 인증 : 로그인 없이 발송 가능하던 문제, 번호 형식 검증, 재발송 대기/시도 횟수 제한 추가
- Gemini 프롬프트 `\\n\\n` (글자 그대로 전송됨) → 실제 줄바꿈, 로그인 확인/입력 검증/타임아웃 추가

## 3. 이름(오타) 정리 — 수업 코드와 동일하게
`Payment` 패키지 → `payment`, `intercepter` → `interceptor`, `SessionUserModleInterceptor` → `SessionUserModelInterceptor`,
`MyDataUtil` → `MyDateUtil`, `GlobalException` → `GlobalExceptionHandler`, `GoogleLogin()` → `googleLogin()`
