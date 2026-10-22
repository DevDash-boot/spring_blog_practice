package com.tenco.spring_blog_practice.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeJpaRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoard_IdAndUser_Id(Long boardId, Long userId);
    long countByBoard_Id(Long boardId);

    // 특정 게시글의 좋아요 전체 삭제
    void deleteByBoard_Id(Long boardId);

    boolean existsByBoard_IdAndUser_Id(Long boardId, Long userId);
}