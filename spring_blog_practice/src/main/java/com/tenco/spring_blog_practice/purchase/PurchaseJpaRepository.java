package com.tenco.spring_blog_practice.purchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseJpaRepository extends JpaRepository<Purchase, Long> {
    // 구매 내역 조회(해당 게시글 PK 기준)
    @Query("select p from Purchase p where p.user.id = :userId and p.board.id = :boardId")
    Optional<Purchase> findByUserIdAndBoardId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    // 구매 여부 확인(true, false로 응답 필요할 때)
    @Query("""
            select case when count(p) > 0 then true else false end
            from Purchase p
            where p.user.id = :userId and p.board.id = :boardId
            """)
    boolean existsByUserIdAndBoardId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    // 게시글을 구매한 건수(게시글 삭제 가능 여부 확인)
    @Query("select count(p) from Purchase p where p.board.id = :boardId")
    long countByBoardId(@Param("boardId") Long boardId);

    // 게시글에 달린 구매 내역 일괄 삭제(게시글 삭제 전에 사용)
    @Modifying
    @Query("delete from Purchase p where p.board.id = :boardId")
    int deleteByBoardId(@Param("boardId") Long boardId);

    // 로그인 사용자 구매 내역 조회(최신 순, 페이징)
    // 게시글 작성자 이름까지 화면에 보여주므로 board와 board.user를 함께 가져온다.
    @Query(value = """
                select p from Purchase p
                join fetch p.board b
                join fetch b.user
                where p.user.id = :userId
                order by p.id desc
            """,
            countQuery = """
                select count(p) from Purchase p
                where p.user.id = :userId
            """)
    Page<Purchase> findAllByUserIdJoinBoard(@Param("userId") Long userId, Pageable pageable);
}
