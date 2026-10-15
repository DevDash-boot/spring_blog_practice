package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice.reply.ReplyJpaRepository;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import com.tenco.spring_blog_practice.user.User;
import com.tenco.spring_blog_practice.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardLikeJpaRepository boardLikeJpaRepository;
    private final ReplyJpaRepository replyJpaRepository;

    // 게시글 저장
    @Transactional
    public Board save(BoardRequest.saveDto saveDto, SessionUserDto sessionUser) {
        log.info("게시글 저장 시작 - 제목 : {}, 작성자 : {}", saveDto.getTitle(), sessionUser.getUsername());
        User user = userJpaRepository.getReferenceById(sessionUser.getId());
        Board savedBoard = boardJpaRepository.save(saveDto.toEntity(user));
        log.info("게시글 저장 완료 - ID : {}, 제목 : {}", savedBoard.getId(), savedBoard.getTitle());
        return savedBoard;
    }

    // 게시글 상세 조회
    public BoardResponse.DetailDto findById(Long id) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        BoardResponse.DetailDto detailDto = new BoardResponse.DetailDto(board);
        long likeCount = boardLikeJpaRepository.countByBoard_Id(id);
        log.info("게시글 ID: {}, 좋아요 개수: {}", id, likeCount);
        detailDto.setLikeCount(Math.toIntExact(likeCount));
        return detailDto;
    }

    // 게시글 목록 조회
    public BoardResponse.PageDto findAllPage(int page, int size, String keyword) {
        log.info("게시글 목록 조회 시작 ");
        // 1부터 시작하는 번호를 0부터로 변환
        int pageIndex = Math.max(0, page - 1);
        // 페이지 크기는 1 ~ 50 정도로 제한(size = 100000 같은 요청 방어)
        int validSize = Math.max(1, Math.min(size, 50));
        // 최신글이 먼저 오도록 id를 내림차순으로 정렬. 주문서 생성
        Pageable pageable = PageRequest.of(pageIndex, validSize, Sort.by(Sort.Direction.DESC, "id"));
        // isBlank() : null이 아닌 상태에서 공백 제거 후 개수 확인
        Page<Board> boardPage;
        if(keyword == null || keyword.isBlank()){
            // 페이징 처리가 된 게시글 목록 조회 요청
            boardPage = boardJpaRepository.findAllJoinUser(pageable);
        } else {
            boardPage = boardJpaRepository.findAllKeywordJoinUser(pageable, keyword);
        }
        // 조회 후 트랜잭션 안에서 응답 DTO로 변환 처리
        return new BoardResponse.PageDto(boardPage);
    }

    public BoardResponse.updateFormDto findByIdAndUpdateForm(Long id, Long userId) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if (!board.isOwner(userId)) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }
        return new BoardResponse.updateFormDto(board);
    }

    @Transactional
    public Board updateById(Long id, BoardRequest.updateDto updateDto, SessionUserDto sessionUser) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정할 수 있습니다..");
        }
        board.update(updateDto);
        return board;
    }

    @Transactional
    public void deleteById(Long id, SessionUserDto sessionUser) {
        // 1. 게시글 조회
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        // 2. 작성자 권한 확인
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }
        // 3. 댓글 삭제
        replyJpaRepository.deleteByBoard_Id(id);
        // 4. 좋아요 삭제
        boardLikeJpaRepository.deleteByBoard_Id(id);
        // 5. 게시글 삭제
        boardJpaRepository.delete(board);
    }

    @Transactional
    public int like(Long boardId, SessionUserDto sessionUser) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        Long userId = sessionUser.getId();

        var existingLike = boardLikeJpaRepository.findByBoard_IdAndUser_Id(boardId, userId);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀다면 취소
            boardLikeJpaRepository.delete(existingLike.get());
        } else {
            // 좋아요 기록 등록
            User user = userJpaRepository.getReferenceById(userId);
            boardLikeJpaRepository.save(new BoardLike(board, user));
        }

        return Math.toIntExact(boardLikeJpaRepository.countByBoard_Id(boardId));
    }
}
