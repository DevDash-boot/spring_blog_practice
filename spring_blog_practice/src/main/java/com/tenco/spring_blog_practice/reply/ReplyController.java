package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.board.BoardService;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
    public String save(ReplyRequest.saveDto saveDto, @RequestParam Long boardId, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return "redirect:/login";
        }
        replyService.save(saveDto, sessionUserDto, boardId);
        return "redirect:/board/" + boardId;
    }

    // 댓글 수정
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, ReplyRequest.updateDto updateDto, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return "redirect:/login";
        }
        Long boardId = replyService.updateById(id, updateDto, sessionUserDto);
        return "redirect:/board/" + boardId;
    }

    // 댓글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return "redirect:/login";
        }
        Long boardId = replyService.deleteById(id, sessionUserDto);
        return "redirect:/board/" + boardId;
    }
}