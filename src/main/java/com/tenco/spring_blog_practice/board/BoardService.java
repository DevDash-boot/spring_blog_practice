package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice.user.User;
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

    @Transactional
    public Board save(BoardRequest.saveDto saveDto, User sessionUser) {
        Board board = saveDto.toEntity(sessionUser);
        Board saveBoard = boardJpaRepository.save(board);
        return saveBoard;
    }

    public Board findById(Long id) {
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> {
                    log.warn("게시글 조회 실패");
                    throw new Exception404("게시글을 찾을 수 없습니다.");
                });
        return board;
    }

    public List<Board> findAll() {
        List<Board> boardList = boardJpaRepository.findAllJoinUser();
        return boardList;
    }

    @Transactional
    public Board updateById(Long id, BoardRequest.updateDto updateDto, User sessionUser) {
        Board board = findById(id);
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정할 수 있습니다..");
        }
        board.update(updateDto);
        return board;
    }

    @Transactional
    public void deleteById(Long id, User sessionUser) {
        Board board = findById(id);
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다..");
        }
        boardJpaRepository.deleteById(id);
    }

    @Transactional
    public Board like(Long id) {
        Board board = findById(id);
        board.setLikeCount(board.getLikeCount() + 1);
        return board;
    }
}
