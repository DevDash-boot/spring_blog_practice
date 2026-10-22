package com.tenco.spring_blog_practice.board;

import com.tenco.spring_blog_practice._core.util.MyDataUtil;
import com.tenco.spring_blog_practice.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_tb")
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public String getTime() {
        return MyDataUtil.timestampFormat(createdAt);
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public void update(BoardRequest.updateDto updateDto) {
        updateDto.validate();
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }
}
