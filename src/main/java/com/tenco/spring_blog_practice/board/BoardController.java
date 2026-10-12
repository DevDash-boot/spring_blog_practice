package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.util.Define;
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

    @GetMapping({"/", "/board/list"})
    public String list(Model model) {
        List<Board> boardList = boardService.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id,
                         Model model,
                         HttpSession session) {
        Board board = boardService.findById(id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        boolean isOwner = sessionUser != null
                && board.isOwner(sessionUser.getId());

        model.addAttribute("board", board);
        model.addAttribute("isOwner", isOwner);

        return "board/detail";
    }

    @GetMapping("/board/save")
    public String saveForm() {
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(HttpSession session, BoardRequest.saveDto saveDto) {
        saveDto.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.save(saveDto, sessionUser);
        return "redirect:/";
    }

    @GetMapping("/board/{id}/update")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        Board board = boardService.findById(id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("수정할 권한이 없습니다.");
        }
        model.addAttribute("board", board);
        return "board/update-form";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Long id, BoardRequest.updateDto updateDto, HttpSession session) {
        updateDto.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.updateById(id, updateDto, sessionUser);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id, sessionUser);
        return "redirect:/";
    }
}
