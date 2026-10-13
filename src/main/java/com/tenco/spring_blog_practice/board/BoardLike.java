package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "board_like_tb",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_board_like_user",
                        columnNames = {"board_id", "user_id"}
                )
        }
)
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BoardLike(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}