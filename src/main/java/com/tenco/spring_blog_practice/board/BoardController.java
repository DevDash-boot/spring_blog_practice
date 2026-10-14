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

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping({"/", "/board/list"})
//    public String list(Model model) {
//        List<BoardResponse.ListDto> boardList = boardService.findAll();
//        model.addAttribute("boardList", boardList);
//        return "board/list";
//    }
    public String list(@RequestParam(defaultValue = "0")int page,
                       @RequestParam(defaultValue = "10") int size,  Model model){
        var boardPage = boardService.findAll(page, size);   // 요청한 페이지의 게시글 조회

        model.addAttribute("boardList", boardPage.getContent());    // 현재 페이지의 게시글 목록
        model.addAttribute("currentPage", boardPage.getNumber());   // 현재 페이지 번호(0부터 시작)
        model.addAttribute("totalPages", boardPage.getTotalPages());    // 전체 페이지 수
        model.addAttribute("hasPrevious", boardPage.hasPrevious()); // 이전 페이지 존재 여부
        model.addAttribute("hasNext", boardPage.hasNext());         // 다음 페이지 존재 여부
        model.addAttribute("previousPage", page - 1);   // 이전 페이지 번호
        model.addAttribute("nextPage", page + 1);       // 다음 페이지 번호
        model.addAttribute("size", size);   // 페이지 당 게시글 수

        // 화면에 표시할 페이지 번호 목록
        List<java.util.Map<String, Object>> pageNumbers = new java.util.ArrayList<>();

        // 반복문으로 페이지 번호 생성하기
        // 전체 페이지 수만큼 반복하면서 각 페이지읭 정보를 목록에 추가
        // "active", i == boardPage.getNumber() : 반복 중인 페이지 번호 i가 현재 조회한 페이지 번호와 같으면 true
        for (int i = 0; i < boardPage.getTotalPages(); i++) {
            pageNumbers.add(java.util.Map.of(
                    "page", i,
                    "displayNumber", i + 1,
                    "active", i == boardPage.getNumber()
            ));
        }

        model.addAttribute("pageNumbers", pageNumbers);

        return "board/list";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model, HttpSession session) {
        BoardResponse.DetailDto detailDto = boardService.findById(id);
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        boolean isOwner = sessionUserDto != null && detailDto.getUserId().equals(sessionUserDto.getId());

        // 게시글별 댓글 조회
        Long sessionUserId = sessionUserDto != null ? sessionUserDto.getId() : null;
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

