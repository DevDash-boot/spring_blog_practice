package com.tenco.spring_blog_practice.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.user u where b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id")Long id);

    @Query("select b from Board b join fetch b.user u order by b.id desc")
    List<Board>findAllJoinUser();
    // 단건 조회 + 작성자 JOIN FETCH
// 전체 조회 + 작성자 JOIN FETCH
}
