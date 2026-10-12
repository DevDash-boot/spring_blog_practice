package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.util.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public String joinForm() {
        return "user/join-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.joinDto joinDto) {
        joinDto.validate();
        userService.join(joinDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.loginDto loginDto, HttpSession session) {
        loginDto.validate();
        User user = userService.login(loginDto);
        user.setPassword(null);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    @GetMapping("/user/update")
    public String updateForm(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.updateDto updateDto, HttpSession session) {
        updateDto.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User user = userService.updateById(sessionUser.getId(), updateDto);
        user.setPassword(null);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
