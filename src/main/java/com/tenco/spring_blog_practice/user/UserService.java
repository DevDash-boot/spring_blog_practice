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
    public UserResponse.JoinDto join(UserRequest.joinDto joinDto) {
        log.info("회원가입 서비스 처리 시작 - 사용자명 : {}", joinDto.getUsername());
        // 중복 체크 : 이미 있으면 null이 아님
        userJpaRepository.findByUsername(joinDto.getUsername())
                .ifPresent(user -> {
                    log.warn("회원 가입 실패 - 중복된 사용자명 : {}", joinDto.getUsername());
                    throw new Exception400("이미 존재하는 사용자명입니다.");
                });
        // 이메일 중복 검사
        userJpaRepository.findByEmail(joinDto.getEmail())
                .ifPresent(user -> {
                    log.warn("회원 가입 실패 - 중복된 이메일 : {}", joinDto.getEmail());
                    throw new Exception400("이미 존재하는 이메일입니다.");
                });

        // 저장
        User savedUser = userJpaRepository.save(joinDto.toEntity());
        log.info("회원가입 서비스 처리 완료 - 사용자명 : {}, ID : {}", joinDto.getUsername(), savedUser.getId());
        return new UserResponse.JoinDto(savedUser);
    }

    public SessionUserDto login(UserRequest.loginDto loginDto) {
        User user = userJpaRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() -> {
                    log.warn("로그인 실패");
                    throw new Exception400("사용자명 또는 비밀번호가 일치하지 않습니다.");
                });
        log.info("로그인 성공 : {}", loginDto.getUsername());
        return new SessionUserDto(user);
    }

    public UserResponse.updateFormDto findUpdateForm(Long userId) {
        return new UserResponse.updateFormDto(findById(userId));
    }

    @Transactional
    public SessionUserDto updateById(Long id, UserRequest.updateDto updateDto) {
        User user = findById(id);
        if (updateDto.getPassword() != null && !updateDto.getPassword().trim().isEmpty()) {
            user.update(updateDto.getPassword().trim());
        }
        return new SessionUserDto(user);
    }

    private User findById(Long id) {
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));
    }
}
