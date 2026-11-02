package com.tenco.spring_blog_practice.admin;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice._core.util.PageResponse;
import com.tenco.spring_blog_practice.refund.PortOneRefundService;
import com.tenco.spring_blog_practice.refund.RefundResponse;
import com.tenco.spring_blog_practice.refund.RefundService;
import com.tenco.spring_blog_practice.refund.RefundStatus;
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
public class AdminController {
    private final RefundService refundService;
    private final PortOneRefundService portOneRefundService;

    // GET - http://localhost:8080/admin/dashboard
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        model.addAttribute("adminName", sessionUserDto.getUsername());
        // 환불 요청 상태별 건수(대기 건수는 처리가 필요한 일이므로 대시보드에서 바로 보여준다.)
        model.addAttribute("refundCount", refundService.countByStatus());
        return "admin/dashboard";
    }

    // 관리자 환불 요청 관리
    // GET - http://localhost:8080/admin/refund/list?status=PENDING&page=1
    @GetMapping("/admin/refund/list")
    public String refundList(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "10") int size,
                             @RequestParam(name = "status", defaultValue = "") String status,
                             Model model) {
        RefundStatus refundStatus = RefundStatus.from(status);  // 값이 없거나 잘못되면 null -> 전체 조회
        PageResponse<RefundResponse.AdminListDto> refundPage = refundService.findAllForAdmin(refundStatus, page, size);
        model.addAttribute("refundPage", refundPage);
        // 상태 필터 탭의 활성화 표시용
        model.addAttribute("filterAll", refundStatus == null);
        model.addAttribute("filterPending", refundStatus == RefundStatus.PENDING);
        model.addAttribute("filterApproved", refundStatus == RefundStatus.APPROVED);
        model.addAttribute("filterRejected", refundStatus == RefundStatus.REJECTED);
        return "admin/refund-list";
    }

    @PostMapping("/admin/refund/{id}/reject")
    public String rejectRefund(@PathVariable(name = "id") Long id,
                               @RequestParam(name = "rejectReason") String rejectReason) {
        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            throw new Exception400("거절 사유를 반드시 입력하세요.");
        }
        if (rejectReason.length() > 500) {
            throw new Exception400("거절 사유는 500자 이하로 입력해주세요.");
        }
        refundService.reject(id, rejectReason.trim());
        return "redirect:/admin/refund/list";
    }

    @PostMapping("/admin/refund/{id}/approve")
    public String approveRefund(@PathVariable(name = "id") Long id) {
        portOneRefundService.approve(id);
        return "redirect:/admin/refund/list";
    }
}
