package com.tenco.spring_blog_practice.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
    @Query("SELECT r FROM Reply r WHERE r.board.id = :boardId ORDER BY r.id DESC")
    List<Reply> findAllByBoardId(@Param("boardId") Long boardId);
}
