package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception401;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.oauth.GoogleLoginService;
import com.tenco.spring_blog_practice.oauth.KakaoLoginService;
import com.tenco.spring_blog_practice.oauth.NaverLoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    private final KakaoLoginService kakaoLoginService;
    private final GoogleLoginService googleLoginService;
    private final NaverLoginService naverLoginService;

    @GetMapping("/kakao-login")
    public String kakaoLogin() {
        return "redirect:" + kakaoLoginService.getAuthorizeUrl();
    }

    // 리다이랙트로 들어와서 카카오 서버가 코드값을 동적으로 뽑아주는 URL
    @GetMapping("/oauth/kakao")
    public String kakaoCallback(@RequestParam(name = "code", required = false) String code, HttpSession session) {
        SessionUserDto sessionUserDto = kakaoLoginService.login(code);
        session.setAttribute(Define.SESSION_USER, sessionUserDto);
        return "redirect:/";
    }

    @GetMapping("/google-login")
    public String GoogleLogin() {
        return "redirect:" + googleLoginService.getAuthorizeUrl();
    }

    @GetMapping("/oauth/google")
    public String googleCallback(@RequestParam(name = "code", required = false) String code, HttpSession session) {
        SessionUserDto sessionUserDto = googleLoginService.login(code);
        session.setAttribute(Define.SESSION_USER, sessionUserDto);
        return "redirect:/";
    }

    @GetMapping("/naver-login")
    public String naverLogin(HttpSession session) {
        String state = UUID.randomUUID().toString();
        session.setAttribute("naverState", state);
        return "redirect:" + naverLoginService.getAuthorizeUrl(state);
    }

    @GetMapping("/oauth/naver")
    public String naverCallback(@RequestParam(name = "code", required = false) String code, @RequestParam String state, HttpSession session) {
        // state 검증
        String savedState = (String) session.getAttribute("naverState");
        if (savedState == null || !savedState.equals(state)) {
            throw new Exception401("네이버 로그인 state 검증에 실패했습니다.");
        }
        session.removeAttribute("naverState");
        SessionUserDto sessionUserDto = naverLoginService.login(code, state);
        session.setAttribute(Define.SESSION_USER, sessionUserDto);
        return "redirect:/";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "user/join-form";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(UserRequest.joinDto joinDto, HttpSession session) {
        joinDto.validate();
        // 이메일 인증 완료된 값 꺼내오기(세션에 저장된 상태)
        String verifiedEmail = (String) session.getAttribute(Define.VERIFIED_EMAIL);
        userService.join(joinDto, verifiedEmail);
        // 가입 완료 후 임시 저장해둔 세션 이메일 정보 제거
        session.removeAttribute(Define.VERIFIED_EMAIL);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.loginDto dto, HttpSession session, Model model) {
        try {
            dto.validate();
            SessionUserDto sessionUser = userService.login(dto);
            session.setAttribute(Define.SESSION_USER, sessionUser);
            return "redirect:/";
        } catch (Exception400 e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/login-form";
        }
    }

    @GetMapping("/user/update")
    public String updateForm(HttpSession session, Model model) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        UserResponse.updateFormDto userUpdateFormDto = userService.findUpdateForm(sessionUserDto.getId());
        model.addAttribute("user", userUpdateFormDto);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(HttpSession session, UserRequest.updateDto updateDto) {
        updateDto.validate();
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        SessionUserDto updatesessionUserDto = userService.updateById(sessionUserDto.getId(), updateDto);
        session.setAttribute(Define.SESSION_USER, updatesessionUserDto);
        return "redirect:/user/detail";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // GET http://localhost:8080/user/detail
    @GetMapping("/user/detail")
    public String detail(Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        UserResponse.DetailDto user = userService.findDetail(sessionUserDto.getId());
        model.addAttribute("user", user);
        return "user/detail";
    }

    // 프로필 삭제
    @PostMapping("/user/profile-image/delete")
    public String deleteProfileImage(HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        userService.deleteProfileImage(sessionUserDto.getId());
        return "redirect:/user/detail";
    }

    // GET http://localhost:8080/user/point/charge (포인트 충전 화면)
    @GetMapping("/user/point/charge")
    public String chargePointForm(Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return "redirect:/login";
        }
        User user = userService.findById(sessionUserDto.getId());
        model.addAttribute("user", user);
        return "user/charge-point";
    }
}
