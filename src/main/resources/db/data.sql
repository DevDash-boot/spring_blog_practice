-- 1. 사용자 (외래키가 가리킬 대상이므로 먼저 넣습니다)
-- 비밀번호는 지금은 연습용 평문입니다. 암호화는 나중에 적용합니다
INSERT INTO user_tb (username, password, email, created_at)
VALUES ('admin', '1234', 'admin@tenco.com', NOW());
INSERT INTO user_tb (username, password, email, created_at)
VALUES ('minsu', '1234', 'minsu@tenco.com', NOW());
INSERT INTO user_tb (username, password, email, created_at)
VALUES ('jihun', '1234', 'jihun@tenco.com', NOW());
INSERT INTO user_tb (username, password, email, created_at)
VALUES ('seoyeon', '1234', 'seoyeon@tenco.com', NOW());

-- 2. 게시글 (user_id는 위 사용자의 id)
-- admin(1)
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('블로그 개설을 환영합니다', '새 블로그가 문을 열었습니다. 많은 참여 부탁드립니다.', 1, NOW(), 12);
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('이용 수칙 안내', '서로 존중하는 소통 문화를 함께 만들어 가요.', 1, NOW(), 5);

-- minsu(2)
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('스프링 부트 학습 후기', 'JPA를 배우니 SQL을 직접 쓰는 일이 확 줄었습니다.', 2, NOW(), 8);
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('연관관계 정리 노트', '@ManyToOne과 외래키의 관계를 정리해 봤습니다.', 2, NOW(), 3);
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('코딩 테스트 문제 추천', '알고리즘 공부에 좋은 문제들을 모아 봤습니다.', 2, NOW(), 17);

-- jihun(3)
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('프론트엔드 프레임워크 비교', '리액트와 뷰의 장단점을 비교해 봤습니다.', 3, NOW(), 10);
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('신입 개발자 취업 팁', '포트폴리오에서 가장 중요했던 점을 공유합니다.', 3, NOW(), 20);

-- seoyeon(4)
INSERT INTO board_tb (title, content, user_id, created_at, like_count)
VALUES ('첫 번째 게시글입니다', '블로그에 처음 글을 올려 봅니다. 자주 소통해요.', 4, NOW(), 0);

-- 3. 댓글 (user_id는 댓글 작성자, board_id는 댓글이 달린 게시글)
-- 게시글 1번: 블로그 개설을 환영합니다
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('블로그 개설을 축하합니다!', 2, 1, NOW());

INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('앞으로 좋은 글 기대하겠습니다.', 3, 1, NOW());

INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('저도 열심히 참여하겠습니다.', 4, 1, NOW());

-- 게시글 2번: 이용 수칙 안내
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('서로 존중하는 문화가 중요하죠.', 2, 2, NOW());

INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('좋은 안내 감사합니다.', 4, 2, NOW());

-- 게시글 3번: 스프링 부트 학습 후기
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('JPA를 배우니 정말 편리한 것 같아요.', 1, 3, NOW());

INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('저도 연관관계 매핑이 가장 어려웠습니다.', 3, 3, NOW());

-- 게시글 4번: 연관관계 정리 노트
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('정리해 주셔서 감사합니다!', 1, 4, NOW());

-- 게시글 5번: 코딩 테스트 문제 추천
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('추천해 주신 문제부터 풀어보겠습니다.', 4, 5, NOW());

-- 게시글 6번: 프론트엔드 프레임워크 비교
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('리액트와 뷰의 비교 내용이 궁금하네요.', 2, 6, NOW());

-- 게시글 7번: 신입 개발자 취업 팁
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('포트폴리오 준비에 도움이 될 것 같습니다.', 1, 7, NOW());

-- 게시글 8번: 첫 번째 게시글입니다
INSERT INTO reply_tb (content, user_id, board_id, created_at)
VALUES ('첫 게시글 작성 축하드립니다!', 2, 8, NOW());