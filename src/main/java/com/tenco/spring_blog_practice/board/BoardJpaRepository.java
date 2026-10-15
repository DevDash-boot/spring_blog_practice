package com.tenco.spring_blog_practice.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.user u where b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);

//    @Query("select b from Board b join fetch b.user u order by b.id desc")
//    List<Board> findAllJoinUser();

    // value : 현재 페이지에 표시할 게시글
    // countQuery : 전체 게시글 수 조회
    @Query(value = "select b from Board b join fetch b.user u order by b.id desc",
            countQuery = "select count(b) from Board b")
    Page<Board> findAllJoinUser(Pageable pageable); // 페이징을 지원하는 메서드로 변경

    // 게시글 검색 + 페이징 조회
    @Query(value = """
            select b from Board b join fetch b.user u
            where b.title like concat ('%', :keyword ,'%')
            or b.content like concat ('%', :keyword ,'%')""",
            countQuery = """
            select count(b) from Board b
            where b.title like concat ('%', :keyword ,'%')
            or b.content like concat ('%', :keyword ,'%')""")
    Page<Board> findAllKeywordJoinUser(Pageable pageable, @Param ("keyword") String keyword);
}
