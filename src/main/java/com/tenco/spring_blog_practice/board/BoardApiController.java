package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.error.Exception401;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("/api/boards/{id}/like")
    public BoardResponse.LikeDto like(@PathVariable Long id, HttpSession session) {
        SessionUserDto sessionUser = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUser == null) {
            throw new Exception401("로그인이 필요합니다.");
        }
        return boardService.like(id, sessionUser);
    }
}