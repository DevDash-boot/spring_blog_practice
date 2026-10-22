package com.tenco.spring_blog_practice.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {
    @Data
    public static class DetailDto {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String username;
        private String createdAt;
        private int likeCount;
        private boolean liked;

        public DetailDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            if (board.getUser() != null) {
                this.userId = board.getUser().getId();
                this.username = board.getUser().getUsername();
            }
            if (board.getCreatedAt() != null) {
                this.createdAt = board.getTime();
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class LikeDto {
        private boolean liked;
        private int likeCount;
    }

    // 게시글 목록 + 페이지 정보 응답 DTO
    @Data
    public static class PageDto {
        private List<ListDto> content;
        private int size;
        private int currentPage;
        private int totalPages;
        private Long totalElements;
        private boolean hasPrevious;
        private boolean hasNext;
        private List<PageLink> pagesLinks;
        private int previousPage;
        private int nextPage;

        public PageDto(Page<Board> page) {
            this.content = page.getContent().stream().map(ListDto::new).toList();

            this.size = page.getSize();
            this.currentPage = page.getNumber() + 1;
            this.totalPages = page.getTotalPages();
            this.totalElements = page.getTotalElements();
            this.hasPrevious = page.hasPrevious();
            this.hasNext = page.hasNext();
            this.pagesLinks = makePageLinks(currentPage, totalPages);
            this.previousPage = currentPage - 1;
            this.nextPage = currentPage + 1;
        }

        // 현재 페이지 기준 앞뒤 2개씩, 최대 5개의 페이지 번호 생성
        private List<PageLink> makePageLinks(int currentPage, int totalPages) {
            List<PageLink> links = new ArrayList<>();
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, currentPage + 2);
            for (int i = startPage; i <= endPage; i++) {
                links.add(new PageLink(i, i == currentPage));
            }
            return links;
        }
    }

    // 페이지 번호 하나의 정보
    @Data
    @AllArgsConstructor
    public static class PageLink {
        private int number;
        private boolean active;
    }

    @Data
    public static class ListDto {
        private Long id;
        private String title;
        private String username;
        private String createdAt;

        public ListDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            if (board.getUser() != null) {
                this.username = board.getUser().getUsername();
            }
            if (board.getCreatedAt() != null) {
                this.createdAt = board.getTime();
            }
        }
    }

    @Data
    public static class updateFormDto {
        private Long id;
        private String title;
        private String content;

        public updateFormDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
