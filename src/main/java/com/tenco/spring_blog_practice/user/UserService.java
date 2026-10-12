package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public User join(UserRequest.joinDto joinDto) {
        userJpaRepository.findUsername(joinDto.getUsername())
                .ifPresent(user -> {
                    log.warn("회원 가입 실패 - 중복된 사용자명 : {}", joinDto.getUsername());
                    throw new Exception400("이미 존재하는 사용자명입니다.");
                });
        userJpaRepository.findEmail(joinDto.getEmail())
                .ifPresent(user -> {
                    log.warn("회원 가입 실패 - 중복된 이메일 : {}", joinDto.getEmail());
                    throw new Exception400("이미 존재하는 이메일입니다.");
                });
        User savedUser = userJpaRepository.save(joinDto.toEntity());
        log.info("회원 가입 - 사용자명 : {}", joinDto.getUsername());
        return savedUser;
    }

    public User login(UserRequest.loginDto loginDto) {
        User user = userJpaRepository.findUnP(loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() -> {
                    log.warn("로그인 실패");
                    throw new Exception400("사용자명 또는 비밀번호가 일치하지 않습니다.");
                });
        log.info("로그인 성공 : {}", loginDto.getUsername());
        return user;
    }

    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(() -> {
            throw new Exception404("사용자를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public User updateById(Long id, UserRequest.updateDto updateDto) {
        User user = findById(id);
        user.update(updateDto.getPassword());
        return user;
    }
}
