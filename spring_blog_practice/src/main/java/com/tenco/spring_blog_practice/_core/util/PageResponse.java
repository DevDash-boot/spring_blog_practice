package com.tenco.spring_blog_practice._core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * 목록 화면 공통 페이징 응답 DTO
 * - 결제 내역, 환불 내역, 구매 내역, 관리자 환불 관리 등에서 함께 사용한다.
 * - 화면(Mustache)에서는 layout/pagination 조각 파일로 페이지 번호를 그린다.
 *
 * 사용 예)  {{#paymentPage}}{{> layout/pagination}}{{/paymentPage}}
 */
@Getter
public class PageResponse<T> {
    public static final int DEFAULT_SIZE = 5;   // 한 페이지에 보여줄 기본 개수
    public static final int MAX_SIZE = 50;      // size=100000 같은 요청 방어

    private final List<T> content;          // 현재 페이지의 데이터
    private final int currentPage;          // 현재 페이지(1부터 시작)
    private final int size;                 // 한 페이지 크기
    private final int totalPages;           // 전체 페이지 수
    private final long totalElements;       // 전체 데이터 수
    private final boolean hasPages;         // 페이지 이동 버튼이 필요한지(2페이지 이상)
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final int previousPage;
    private final int nextPage;
    private final List<PageLink> pageLinks; // 화면에 보여줄 페이지 번호들
    private final String baseUrl;           // 페이지 링크 앞부분 (예: /user/payment/list?size=5&)

    /**
     * @param page    Spring Data 조회 결과(페이지 정보를 꺼내기 위해 사용)
     * @param content 화면에 내려줄 DTO 목록(엔티티를 DTO로 변환한 결과)
     * @param baseUrl 페이지 링크의 앞부분. 마지막은 ? 또는 & 로 끝나야 한다.
     */
    public PageResponse(Page<?> page, List<T> content, String baseUrl) {
        this.content = content;
        this.currentPage = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.hasPages = totalPages > 1;
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.previousPage = currentPage - 1;
        this.nextPage = currentPage + 1;
        this.pageLinks = makePageLinks(currentPage, totalPages);
        this.baseUrl = baseUrl;
    }

    // 요청 파라미터(page는 1부터, size는 1 ~ MAX_SIZE)를 안전한 Pageable로 변환
    // 정렬은 각 Repository의 쿼리(order by)에서 처리한다.
    public static Pageable pageable(int page, int size) {
        int pageIndex = Math.max(0, page - 1);
        int validSize = Math.max(1, Math.min(size, MAX_SIZE));
        return PageRequest.of(pageIndex, validSize);
    }

    // 현재 페이지 기준 앞뒤 2개씩, 최대 5개의 페이지 번호 생성
    private static List<PageLink> makePageLinks(int currentPage, int totalPages) {
        List<PageLink> links = new ArrayList<>();
        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, currentPage + 2);
        for (int i = startPage; i <= endPage; i++) {
            links.add(new PageLink(i, i == currentPage));
        }
        return links;
    }

    @Getter
    @AllArgsConstructor
    public static class PageLink {
        private int number;
        private boolean active;
    }
}
