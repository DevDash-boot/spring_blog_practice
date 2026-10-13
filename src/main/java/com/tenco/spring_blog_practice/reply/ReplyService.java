package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice.board.Board;
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
public class ReplyService {
    private final ReplyJpaRepository replyJpaRepository;

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
    public Reply save(ReplyRequest.saveDto saveDto, User sessionUser, Board board) {
        saveDto.validate();

        Reply reply = saveDto.toEntity(sessionUser, board);

        return replyJpaRepository.save(reply);
    }

    // 댓글 수정
    // 댓글 수정
    @Transactional
    public Reply updateById(Long id, ReplyRequest.updateDto updateDto, User sessionUser) {
        Reply reply = findById(id);
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        updateDto.validate();
        reply.update(updateDto);
        return reply;
    }

    // 댓글 삭제
    @Transactional
    public void deleteById(Long id, User sessionUser){
        Reply reply = findById(id);
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 삭제할 수 있습니다..");
        }
        replyJpaRepository.delete(reply);;
    }
}
