-- =========================================
-- 1. 사용자 20명
-- =========================================

-- 사용자 데이터
-- 비밀번호: 1234 (BCrypt 해싱 값)
-- point : 보유 포인트 (유료 게시글 열람 가격 = 500P)
--   admin(10000P), minsu(5000P) : 포인트 보유 (유료 게시글도 작성)
--   포인트 충분(500P 이상) : jihun, seoyeon, yujin, sumin, jiwoo, taeyang, sangho, chaewon, nari, harin
--   포인트 부족(500P 미만) : junho(300P)
--   포인트 없음(0P)        : dohyun, hyunwoo, eunji, yuna, haneul, seungho, geonwoo

INSERT INTO user_tb (username, password, email, point, created_at)
VALUES
    ('admin', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'admin@tenco.com', 10000, NOW()),
    ('minsu', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'minsu@tenco.com', 5000, NOW()),
    ('jihun', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'jihun@tenco.com', 3000, NOW()),
    ('seoyeon', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'seoyeon@tenco.com', 1500, NOW()),
    ('yujin', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'yujin@tenco.com', 500, NOW()),
    ('junho', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'junho@tenco.com', 300, NOW()),
    ('sumin', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'sumin@tenco.com', 2000, NOW()),
    ('dohyun', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'dohyun@tenco.com', 0, NOW()),
    ('jiwoo', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'jiwoo@tenco.com', 800, NOW()),
    ('hyunwoo', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'hyunwoo@tenco.com', 0, NOW()),
    ('eunji', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'eunji@tenco.com', 0, NOW()),
    ('taeyang', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'taeyang@tenco.com', 1000, NOW()),
    ('yuna', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'yuna@tenco.com', 0, NOW()),
    ('sangho', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'sangho@tenco.com', 4000, NOW()),
    ('haneul', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'haneul@tenco.com', 0, NOW()),
    ('chaewon', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'chaewon@tenco.com', 600, NOW()),
    ('seungho', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'seungho@tenco.com', 0, NOW()),
    ('nari', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'nari@tenco.com', 2500, NOW()),
    ('geonwoo', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'geonwoo@tenco.com', 0, NOW()),
    ('harin', '$2a$10$jiByiZruBRyjQuhMgF6kY.Hg2NTqKV2YqmbiGoUYn/HthKtJrkoca', 'harin@tenco.com', 1200, NOW());



-- 1-1. 사용자 역할
-- admin은 관리자이면서 일반 사용자

INSERT INTO user_role_tb (user_id, role) VALUES (1, 'ADMIN');
INSERT INTO user_role_tb (user_id, role) VALUES (1, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (2, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (3, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (4, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (5, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (6, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (7, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (8, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (9, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (10, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (11, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (12, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (13, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (14, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (15, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (16, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (17, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (18, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (19, 'USER');
INSERT INTO user_role_tb (user_id, role) VALUES (20, 'USER');

-- =========================================
-- 2. 게시글 40개 (무료 22개 / 유료 18개)
-- user_id는 위 사용자 등록 순서 기준
-- premium : TRUE = 유료 게시글(500P), FALSE = 무료 게시글
--   admin(1) : 무료 1개 + 유료 1개 / minsu(2) : 무료 1개 + 유료 2개
-- =========================================
INSERT INTO board_tb (title, content, premium, user_id, created_at)
VALUES
    ('블로그 개설을 환영합니다', '새 블로그가 문을 열었습니다. 많은 참여 부탁드립니다.', FALSE, 1, NOW()),
    ('이용 수칙 안내', '서로 존중하는 소통 문화를 함께 만들어 가요.', TRUE, 1, NOW()),
    ('스프링 부트 학습 후기', 'JPA를 배우니 SQL을 직접 쓰는 일이 확 줄었습니다.', FALSE, 2, NOW()),
    ('연관관계 정리 노트', '@ManyToOne과 외래키의 관계를 정리해 봤습니다.', TRUE, 2, NOW()),
    ('코딩 테스트 문제 추천', '알고리즘 공부에 좋은 문제들을 모아 봤습니다.', TRUE, 2, NOW()),
    ('프론트엔드 프레임워크 비교', '리액트와 뷰의 장단점을 비교해 봤습니다.', TRUE, 3, NOW()),
    ('신입 개발자 취업 팁', '포트폴리오에서 가장 중요했던 점을 공유합니다.', FALSE, 3, NOW()),
    ('첫 번째 게시글입니다', '블로그에 처음 글을 올려 봅니다. 자주 소통해요.', FALSE, 4, NOW()),
    ('Java 컬렉션 정리', 'List, Set, Map의 특징과 차이점을 정리했습니다.', TRUE, 5, NOW()),
    ('객체지향 프로그래밍이란', '캡슐화, 상속, 다형성의 개념을 정리해 봤습니다.', FALSE, 6, NOW()),
    ('REST API 설계 원칙', '일관성 있는 API URL을 설계하는 방법을 알아봅니다.', TRUE, 7, NOW()),
    ('Git 브랜치 사용법', '브랜치를 이용해 기능별로 작업을 분리해 봤습니다.', FALSE, 8, NOW()),
    ('MySQL 인덱스 기초', '인덱스를 사용하면 조회 성능을 개선할 수 있습니다.', TRUE, 9, NOW()),
    ('트랜잭션 이해하기', '트랜잭션의 원자성과 일관성에 대해 공부했습니다.', FALSE, 10, NOW()),
    ('HTTP 요청과 응답', '클라이언트와 서버가 데이터를 주고받는 과정을 정리했습니다.', FALSE, 11, NOW()),
    ('CSS Flexbox 정리', 'Flexbox로 요소를 정렬하는 방법을 소개합니다.', TRUE, 12, NOW()),
    ('JavaScript 비동기 처리', 'Promise와 async/await의 차이를 정리했습니다.', FALSE, 13, NOW()),
    ('JPA 지연 로딩 이해', '지연 로딩과 즉시 로딩의 차이를 알아봅니다.', TRUE, 14, NOW()),
    ('DTO를 사용하는 이유', '계층 간 데이터 전달에 DTO를 사용하는 이유입니다.', FALSE, 15, NOW()),
    ('Spring 인터셉터 활용', '요청 처리 전후에 공통 기능을 실행해 봤습니다.', TRUE, 16, NOW()),
    ('예외 처리 방법 정리', '예외 상황을 일관되게 처리하는 방법을 공유합니다.', FALSE, 17, NOW()),
    ('페이징 처리 구현하기', '게시글을 페이지 단위로 나누어 조회하도록 구현했습니다.', TRUE, 18, NOW()),
    ('게시글 검색 기능 만들기', '제목과 내용에 포함된 키워드로 게시글을 검색합니다.', FALSE, 19, NOW()),
    ('Mustache 템플릿 사용법', '서버에서 전달한 데이터를 화면에 출력해 봤습니다.', FALSE, 20, NOW()),
    ('로그를 활용한 디버깅', '로그를 활용해 오류가 발생한 지점을 찾아봅니다.', TRUE, 5, NOW()),
    ('서비스와 레포지토리 역할', '서비스 계층과 레포지토리 계층의 책임을 정리했습니다.', FALSE, 6, NOW()),
    ('데이터베이스 정규화', '중복 데이터를 줄이기 위한 정규화 개념을 알아봅니다.', TRUE, 7, NOW()),
    ('외래키와 참조 무결성', '테이블 사이의 관계를 외래키로 관리해 봤습니다.', FALSE, 8, NOW()),
    ('회원가입 기능 구현', '회원 정보를 입력받아 데이터베이스에 저장합니다.', FALSE, 9, NOW()),
    ('로그인과 세션 관리', '세션을 이용해 로그인 상태를 유지해 봤습니다.', TRUE, 10, NOW()),
    ('게시글 수정과 삭제', '작성자 본인만 게시글을 수정하고 삭제하도록 구현했습니다.', FALSE, 11, NOW()),
    ('댓글 기능 구현하기', '게시글에 댓글을 작성하고 삭제할 수 있도록 만들었습니다.', TRUE, 12, NOW()),
    ('좋아요 기능 구현하기', '사용자별 좋아요 상태를 데이터베이스에 저장합니다.', FALSE, 13, NOW()),
    ('테스트 코드 작성법', 'Given-When-Then 구조로 테스트를 작성해 봤습니다.', TRUE, 14, NOW()),
    ('SQL JOIN 정리', 'INNER JOIN과 LEFT JOIN의 차이를 정리했습니다.', FALSE, 15, NOW()),
    ('개발자 공부 루틴', '매일 일정한 시간 동안 공부하는 습관을 만들고 있습니다.', FALSE, 16, NOW()),
    ('알고리즘 문제 풀이 기록', '오늘 해결한 문제와 풀이 방법을 기록합니다.', TRUE, 17, NOW()),
    ('코드 리뷰의 중요성', '다른 사람의 코드를 읽으며 새로운 관점을 배웠습니다.', FALSE, 18, NOW()),
    ('프로젝트 README 작성', '프로젝트 목적과 실행 방법을 문서로 정리했습니다.', FALSE, 19, NOW()),
    ('다음 프로젝트 계획', '지금까지 배운 내용을 활용해 새로운 기능을 만들어 보겠습니다.', TRUE, 20, NOW());

-- =========================================
-- 3. 댓글 80개
-- 각 게시글에 댓글 2개씩 등록
-- =========================================
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES
    ('좋은 글 감사합니다!', 2, 1, NOW()),
    ('앞으로 자주 방문하겠습니다.', 3, 1, NOW()),
    ('서로 존중하는 문화가 중요하죠.', 4, 2, NOW()),
    ('좋은 안내 감사합니다.', 5, 2, NOW()),
    ('JPA를 배우니 정말 편리한 것 같아요.', 1, 3, NOW()),
    ('저도 공부하면서 많이 배우고 있습니다.', 6, 3, NOW()),
    ('정리해 주셔서 감사합니다.', 7, 4, NOW()),
    ('연관관계는 예제를 만들어 보면 이해가 잘됩니다.', 8, 4, NOW()),
    ('추천해 주신 문제부터 풀어보겠습니다.', 9, 5, NOW()),
    ('알고리즘은 꾸준히 푸는 게 중요한 것 같아요.', 10, 5, NOW()),
    ('리액트와 뷰의 비교 내용이 궁금했어요.', 11, 6, NOW()),
    ('프로젝트에 적용해 봐야겠네요.', 12, 6, NOW()),
    ('포트폴리오 준비에 도움이 될 것 같습니다.', 13, 7, NOW()),
    ('실제 프로젝트 경험도 중요하다고 생각합니다.', 14, 7, NOW()),
    ('첫 게시글 작성 축하드립니다!', 15, 8, NOW()),
    ('앞으로 좋은 글 많이 올려주세요.', 16, 8, NOW()),
    ('컬렉션은 자주 사용하는 부분이라 유용하네요.', 17, 9, NOW()),
    ('Map 예제도 추가해 주시면 좋겠습니다.', 18, 9, NOW()),
    ('객체지향 개념을 다시 공부하게 되네요.', 19, 10, NOW()),
    ('작은 예제를 만들어 보니 이해가 잘됐습니다.', 20, 10, NOW()),
    ('REST API 설계할 때 참고하겠습니다.', 1, 11, NOW()),
    ('URL 이름을 정하는 게 은근히 어렵더라고요.', 2, 11, NOW()),
    ('브랜치를 나누면 협업하기 편리하죠.', 3, 12, NOW()),
    ('저도 기능별 브랜치를 사용하고 있습니다.', 4, 12, NOW()),
    ('인덱스가 성능에 미치는 영향이 궁금합니다.', 5, 13, NOW()),
    ('실행 계획도 함께 확인하면 좋습니다.', 6, 13, NOW()),
    ('트랜잭션 개념이 이제 조금 이해됩니다.', 7, 14, NOW()),
    ('실패했을 때 롤백되는 것도 중요하죠.', 8, 14, NOW()),
    ('HTTP 상태 코드도 함께 정리하면 좋겠네요.', 9, 15, NOW()),
    ('요청과 응답 구조를 이해하는 데 도움이 됩니다.', 10, 15, NOW()),
    ('Flexbox는 레이아웃 만들 때 정말 유용합니다.', 11, 16, NOW()),
    ('Grid와 비교해 보는 것도 추천합니다.', 12, 16, NOW()),
    ('비동기 처리는 예제를 많이 보는 게 좋은 것 같아요.', 13, 17, NOW()),
    ('async/await를 사용하면 코드가 읽기 편해집니다.', 14, 17, NOW()),
    ('지연 로딩은 N+1 문제도 확인해야 합니다.', 15, 18, NOW()),
    ('필요한 데이터를 fetch join으로 가져올 수 있죠.', 16, 18, NOW()),
    ('DTO를 사용하니 화면에 필요한 데이터만 전달할 수 있네요.', 17, 19, NOW()),
    ('엔티티를 직접 반환하지 않는 이유를 알겠습니다.', 18, 19, NOW()),
    ('인터셉터로 로그인 여부를 확인할 수 있겠네요.', 19, 20, NOW()),
    ('공통 처리를 분리할 수 있어서 좋습니다.', 20, 20, NOW()),
    ('예외 메시지를 일관되게 관리하면 편리합니다.', 1, 21, NOW()),
    ('전역 예외 처리도 함께 사용해 보세요.', 2, 21, NOW()),
    ('페이지 번호를 1부터 보여주면 직관적이죠.', 3, 22, NOW()),
    ('검색과 페이징을 함께 적용하면 좋겠습니다.', 4, 22, NOW()),
    ('검색어가 포함된 게시글만 조회하는 방식이군요.', 5, 23, NOW()),
    ('검색 후 페이지 이동도 테스트해 봐야겠네요.', 6, 23, NOW()),
    ('Mustache 문법을 익히는 데 도움이 됩니다.', 7, 24, NOW()),
    ('반복 출력과 조건 출력이 편리하네요.', 8, 24, NOW()),
    ('로그를 남기면 문제를 찾기 수월합니다.', 9, 25, NOW()),
    ('민감한 정보는 로그에 남기지 않도록 주의해야 합니다.', 10, 25, NOW()),
    ('계층별 역할을 구분하는 게 중요하네요.', 11, 26, NOW()),
    ('서비스에 비즈니스 로직을 모으는 방식이 좋습니다.', 12, 26, NOW()),
    ('정규화는 데이터 중복을 줄이는 데 도움이 됩니다.', 13, 27, NOW()),
    ('실제 테이블 구조를 보면서 공부하면 좋겠어요.', 14, 27, NOW()),
    ('외래키가 있으면 잘못된 참조를 막을 수 있겠네요.', 15, 28, NOW()),
    ('삭제할 때 참조 관계도 확인해야 합니다.', 16, 28, NOW()),
    ('회원가입 시 중복 사용자명 검사도 필요합니다.', 17, 29, NOW()),
    ('입력값 검증도 꼭 적용해 보세요.', 18, 29, NOW()),
    ('세션을 사용하면 로그인 상태를 유지할 수 있죠.', 19, 30, NOW()),
    ('로그아웃할 때 세션을 무효화하는 것도 중요합니다.', 20, 30, NOW()),
    ('작성자 권한 확인이 꼭 필요하겠네요.', 1, 31, NOW()),
    ('서버에서도 권한 검사를 해야 안전합니다.', 2, 31, NOW()),
    ('댓글 작성 기능까지 구현하셨군요.', 3, 32, NOW()),
    ('댓글 수정 기능도 추가해 보면 좋겠습니다.', 4, 32, NOW()),
    ('사용자별 좋아요 중복을 막는 게 중요합니다.', 5, 33, NOW()),
    ('좋아요 취소 기능도 있으면 편리하겠네요.', 6, 33, NOW()),
    ('Given-When-Then 구조가 읽기 편하네요.', 7, 34, NOW()),
    ('테스트 데이터 준비도 중요한 것 같습니다.', 8, 34, NOW()),
    ('JOIN 종류를 비교하기 좋게 정리했네요.', 9, 35, NOW()),
    ('실제 데이터로 쿼리를 실행해 보면 더 잘 이해됩니다.', 10, 35, NOW()),
    ('공부 루틴을 꾸준히 유지하는 게 어렵죠.', 11, 36, NOW()),
    ('작은 목표부터 정해 보면 좋겠습니다.', 12, 36, NOW()),
    ('풀이 과정을 기록하는 습관이 좋습니다.', 13, 37, NOW()),
    ('다른 풀이와 비교해 보는 것도 추천합니다.', 14, 37, NOW()),
    ('코드 리뷰를 하면 놓친 부분을 찾을 수 있죠.', 15, 38, NOW()),
    ('서로 피드백을 주고받으면 많이 배울 수 있습니다.', 16, 38, NOW()),
    ('README가 잘 정리되어 있으면 프로젝트 이해가 쉽습니다.', 17, 39, NOW()),
    ('실행 환경과 설정 방법도 작성하면 좋겠네요.', 18, 39, NOW()),
    ('새로운 프로젝트도 기대하겠습니다!', 19, 40, NOW()),
    ('배운 내용을 실제로 적용하는 게 중요하죠.', 1, 40, NOW());

-- =========================================
-- 4. 좋아요 샘플 데이터
-- 같은 사용자-게시글 조합은 중복하지 않음
-- =========================================
INSERT INTO board_like_tb (board_id, user_id)
VALUES
    (1, 2), (1, 3), (1, 4), (1, 5),
    (2, 1), (2, 3), (2, 6),
    (3, 1), (3, 4), (3, 7),
    (4, 1), (4, 5), (4, 8),
    (5, 2), (5, 3), (5, 9),
    (6, 1), (6, 4), (6, 10),
    (7, 2), (7, 5), (7, 11),
    (8, 1), (8, 6), (8, 12),
    (9, 2), (9, 7), (9, 13),
    (10, 3), (10, 8), (10, 14),
    (11, 4), (11, 9), (11, 15),
    (12, 5), (12, 10), (12, 16),
    (13, 6), (13, 11), (13, 17),
    (14, 7), (14, 12), (14, 18),
    (15, 8), (15, 13), (15, 19),
    (16, 9), (16, 14), (16, 20),
    (17, 10), (17, 15), (17, 1),
    (18, 11), (18, 16), (18, 2),
    (19, 12), (19, 17), (19, 3),
    (20, 13), (20, 18), (20, 4),
    (21, 14), (21, 19), (21, 5),
    (22, 15), (22, 20), (22, 6),
    (23, 16), (23, 1), (23, 7),
    (24, 17), (24, 2), (24, 8),
    (25, 18), (25, 3), (25, 9),
    (26, 19), (26, 4), (26, 10),
    (27, 20), (27, 5), (27, 11),
    (28, 1), (28, 6), (28, 12),
    (29, 2), (29, 7), (29, 13),
    (30, 3), (30, 8), (30, 14),
    (31, 4), (31, 9), (31, 15),
    (32, 5), (32, 10), (32, 16),
    (33, 6), (33, 11), (33, 17),
    (34, 7), (34, 12), (34, 18),
    (35, 8), (35, 13), (35, 19),
    (36, 9), (36, 14), (36, 20),
    (37, 10), (37, 15), (37, 1),
    (38, 11), (38, 16), (38, 2),
    (39, 12), (39, 17), (39, 3),
    (40, 13), (40, 18), (40, 4);