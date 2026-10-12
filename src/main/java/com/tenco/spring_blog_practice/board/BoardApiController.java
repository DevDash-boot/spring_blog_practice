package com.tenco.spring_blog_practice.board;

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
    public Map<String, Integer> like(@PathVariable Long id) {
        Board board = boardService.like(id);
        return Map.of("likeCount", board.getLikeCount());
    }
}