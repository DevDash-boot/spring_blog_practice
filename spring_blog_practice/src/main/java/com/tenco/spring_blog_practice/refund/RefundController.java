package com.tenco.spring_blog_practice.refund;


import com.tenco.spring_blog_practice.payment.PaymentResponse;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RefundController {
    private final RefundService refundService;

    // http://localhost:8080/refund/request/{paymentPk}
    @GetMapping("/refund/request/{paymentPk}")
    public String requestForm(@PathVariable Long paymentPk, HttpSession session, Model model) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        PaymentResponse.ListDto paymentListDto = refundService.findRequestForm(paymentPk, sessionUserDto.getId());
        model.addAttribute("payment", paymentListDto);
        return "refund/request-form";
    }

    @PostMapping("/refund/request")
    public String request(RefundRequest.SaveDto saveDto, HttpSession session) {
        saveDto.validate();
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        refundService.save(saveDto, sessionUserDto.getId());
        return "redirect:/refund/list";
    }

    @GetMapping("/refund/list")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        model.addAttribute("refundPage", refundService.findAllByUserId(sessionUserDto.getId(), page, size));
        return "refund/list";
    }
}