package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice._core.util.FileUtil;
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

        // 프로필 이미지 저장(새롭게 생긴 파일 이름이 리턴)
        String profileImage = FileUtil.saveImage(joinDto.getProfileImage());

        // 저장
        User savedUser = userJpaRepository.save(joinDto.toEntity(profileImage));
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

    // 회원 정보 수정 처리(Dirty Checking 활용)
    // 세션 동기화용 DTO 반환
    @Transactional
    public SessionUserDto updateById(Long id, UserRequest.updateDto updateDto) {
        User user = findById(id);   // 정보 조회
        if (updateDto.hasPassword()) {
            user.update(updateDto.getPassword());   // 비밀번호
        }
        if (updateDto.hasProfileImage()) {
            // 새 이미지를 골랐을 때만 교체 (null 또는 파일 데이터
            String newImage = FileUtil.saveImage(updateDto.getProfileImage());
            String oldImage = user.getProfileImage();
            user.updateProfileImage(newImage);  // DB에는 새 파일 이름 변경 감지
            FileUtil.deleteFile(oldImage);  // 기존 파일은 서버 측 폴더에서 삭제처리
        }
        return new SessionUserDto(user);    // 세션 동기화용 DTO 변환
    }

    private User findById(Long id) {
        log.info("사용자 정보 조회 - ID : {}", id);
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다."));
    }

    public UserResponse.DetailDto findDetail(Long userId) {
        User user = findById(userId);
        return new UserResponse.DetailDto(user);
    }

    // 프로필 이미지 삭제
    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = findById(userId);
        String oldImage = user.getProfileImage();
        user.updateProfileImage(null);  // DB에 파일 이름 비우기
        FileUtil.deleteFile(oldImage);  // 폴더에 저장된 파일 삭제
    }
}
