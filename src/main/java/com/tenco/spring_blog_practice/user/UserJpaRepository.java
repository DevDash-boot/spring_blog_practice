package com.tenco.spring_blog_practice.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username and u.password = :password")
    Optional<User> findUnP(@Param("username")String username, @Param("password")String password);

    @Query("select u from User u where u.username = :username")
    Optional<User> findUsername(@Param("username")String username);

    @Query("select u from User u where u.email = :email")
    Optional<User> findEmail(@Param("email")String email);
}
