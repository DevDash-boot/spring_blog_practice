package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice.board.Board;
import com.tenco.spring_blog_practice.board.BoardJpaRepository;
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
public class ReplyService {
    private final ReplyJpaRepository replyJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    // 댓글 목록
    public List<Reply> findAllByBoardId(Long boardId) {
        return replyJpaRepository.findAllByBoardId(boardId);
    }

    public Reply findById(Long id){
        Reply reply = replyJpaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("댓글 조회 실패");
                    throw new Exception404("댓글을 찾을 수 없습니다.");
                });
        return reply;
    }

    // 댓글 등록
    @Transactional
    public Reply save(ReplyRequest.saveDto saveDto, SessionUserDto sessionUser, Long boardId) {
        saveDto.validate();
        // 게시글 조회
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        // 로그인 사용자 조회
        User user = userJpaRepository.getReferenceById(sessionUser.getId());
        // 댓글 저장
        Reply reply = saveDto.toEntity(user, board);
        return replyJpaRepository.save(reply);
    }

    // 댓글 수정
    @Transactional
    public Long updateById(Long id, ReplyRequest.updateDto updateDto, SessionUserDto sessionUser) {
        Reply reply = findById(id);
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        updateDto.validate();
        reply.update(updateDto);
        return reply.getBoard().getId();
    }

    // 댓글 삭제
    @Transactional
    public Long deleteById(Long id, SessionUserDto sessionUser){
        Reply reply = findById(id);
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 삭제할 수 있습니다..");
        }
        Long boardId = reply.getBoard().getId();
        replyJpaRepository.delete(reply);

        return boardId;
    }
}
