package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.board.Board;
import com.tenco.spring_blog_practice.board.BoardService;
import com.tenco.spring_blog_practice.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final BoardService boardService;

    // 댓글 등록
    @PostMapping("/save")
    public String save(
            ReplyRequest.saveDto saveDto,
            @RequestParam Long boardId,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        if (sessionUser == null) {
            return "redirect:/login";
        }

        Board board = boardService.findById(boardId);

        replyService.save(saveDto, sessionUser, board);

        return "redirect:/board/" + boardId;
    }

    // 댓글 수정
    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            ReplyRequest.updateDto updateDto,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        if (sessionUser == null) {
            return "redirect:/login";
        }

        Reply reply = replyService.updateById(id, updateDto, sessionUser);

        return "redirect:/board/" + reply.getBoard().getId();
    }

    // 댓글 삭제
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        if (sessionUser == null) {
            return "redirect:/login";
        }

        Reply reply = replyService.findById(id);
        Long boardId = reply.getBoard().getId();

        replyService.deleteById(id, sessionUser);

        return "redirect:/board/" + boardId;
    }
}