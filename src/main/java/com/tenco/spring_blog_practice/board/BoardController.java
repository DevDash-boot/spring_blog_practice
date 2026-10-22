package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.reply.ReplyResponse;
import com.tenco.spring_blog_practice.reply.ReplyService;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping({"/", "/board/list"})
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "8") int size,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword, Model model) {
        BoardResponse.PageDto pageDto = boardService.findAllPage(page, size, keyword);
        model.addAttribute("boardPage", pageDto);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isSearch", !keyword.isBlank());
        // 페이지 링크는 직접 URL 인코딩이 필요하다.
        model.addAttribute("keywordParam", URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        Long sessionUserId = (sessionUserDto != null) ? sessionUserDto.getId() : null;

        BoardResponse.DetailDto detailDto = boardService.findById(id, sessionUserId);
        boolean isOwner = sessionUserId != null && detailDto.getUserId().equals(sessionUserId);
        List<ReplyResponse> replyResponseList = replyService.findAllByBoardId(id, sessionUserId);

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

