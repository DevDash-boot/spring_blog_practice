package com.tenco.spring_blog_practice.reply;

import com.tenco.spring_blog_practice.board.Board;
import com.tenco.spring_blog_practice.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reply_tb")
@Data
public class Reply {
    // 댓글에 들어갈 내용 : id, 사용자명, 댓글, 댓글단 시간, 외래키 (user, board)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Reply(String content) {
        this.content = content;
    }

    public boolean isOwner(Long userId) {
        return this.user != null && this.user.getId().equals(userId);
    }

    public void update(ReplyRequest.updateDto updateDto) {
        updateDto.validate();
        this.content = updateDto.getContent().trim();
    }
}
