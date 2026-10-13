package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.reply.Reply;
import com.tenco.spring_blog_practice.reply.ReplyResponse;
import com.tenco.spring_blog_practice.reply.ReplyService;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import com.tenco.spring_blog_practice.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping({"/", "/board/list"})
    public String list(Model model) {
        List<BoardResponse.ListDto> boardList = boardService.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        BoardResponse.DetailDto detailDto = boardService.findById(id);
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);

        boolean isOwner = sessionUserDto != null && detailDto.getUserId().equals(sessionUserDto.getId());

        // 게시글별 댓글 조회
        List<Reply> replyList = replyService.findAllByBoardId(id);
        List<ReplyResponse> replyResponseList = replyList.stream()
                .map(reply -> new ReplyResponse(reply, sessionUserDto != null ? sessionUserDto.getId() : null
                ))
                .toList();

        model.addAttribute("board", detailDto);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("replyList", replyResponseList);

        return "board/detail";
    }

    @GetMapping("/board/save")
    public String saveForm() {
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(HttpSession session, BoardRequest.saveDto saveDto) {
        saveDto.validate();
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        boardService.save(saveDto, sessionUserDto);
        return "redirect:/";
    }

    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        BoardResponse.updateFormDto board = boardService.findByIdAndUpdateForm(id, sessionUserDto.getId());
        model.addAttribute("board", board);
        return "board/update-form";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Long id, BoardRequest.updateDto updateDto, HttpSession session) {
        updateDto.validate();
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        boardService.updateById(id, updateDto, sessionUserDto);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id, sessionUserDto);
        return "redirect:/";
    }
}

