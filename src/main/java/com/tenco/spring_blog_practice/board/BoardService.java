package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import com.tenco.spring_blog_practice.user.User;
import com.tenco.spring_blog_practice.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 게시글 저장
    @Transactional
    public Board save(BoardRequest.saveDto saveDto, SessionUserDto sessionUser) {
        log.info("게시글 저장 시작 - 제목 : {}, 작성자 : {}", saveDto.getTitle(), sessionUser.getUsername());
        User user = userJpaRepository.getReferenceById(sessionUser.getId());
        Board savedBoard = boardJpaRepository.save(saveDto.toEntity(user));
        log.info("게시글 저장 완료 - ID : {}, 제목 : {}", savedBoard.getId(), savedBoard.getTitle());
        return savedBoard;
    }

    // 게시글 조회
    public BoardResponse.DetailDto findById(Long id) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> {
                    log.warn("게시글 조회 실패");
                    throw new Exception404("게시글을 찾을 수 없습니다.");
                });
        return new BoardResponse.DetailDto(board);
    }

    // 게시글 목록 조회
    public List<BoardResponse.ListDto> findAll() {
        List<Board> boardList = boardJpaRepository.findAllJoinUser();
        return boardList.stream().map(BoardResponse.ListDto::new).toList();
    }

    public BoardResponse.updateFormDto findByIdAndUpdateForm(Long id, Long userId){
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if(!board.isOwner(userId)){
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
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다..");
        }
        boardJpaRepository.deleteById(id);
    }

    @Transactional
    public Board like(Long id) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));
        board.setLikeCount(board.getLikeCount() + 1);
        return board;
    }
}
