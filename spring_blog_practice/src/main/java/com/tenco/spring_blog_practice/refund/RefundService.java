package com.tenco.spring_blog_practice.refund;

import com.tenco.spring_blog_practice.payment.Payment;
import com.tenco.spring_blog_practice.payment.PaymentJpaRepository;
import com.tenco.spring_blog_practice.payment.PaymentResponse;
import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.error.Exception404;
import com.tenco.spring_blog_practice._core.util.PageResponse;
import com.tenco.spring_blog_practice.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)  // 조회 메서드는 읽기 전용, 저장/수정 메서드는 개별로 @Transactional 선언
public class RefundService {
    private final RefundRepository refundRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    // 환불 요청 폼에 보여줄 결제 정보
    public PaymentResponse.ListDto findRequestForm(Long paymentPk, Long userId) {
        Payment payment = checkRefundable(paymentPk, userId);
        // 환불 가능한 payment 엔터티만 조회 되었기 때문에 무조건 true 처리
        return new PaymentResponse.ListDto(payment, true);
    }

    private Payment checkRefundable(Long paymentPk, Long userId) {
        // payment 테이블 결제 내역 조회
        Payment payment = paymentJpaRepository.findById(paymentPk)
                .orElseThrow(() -> new Exception404("결제 내역을 찾을 수 없습니다."));
        // 본인 결제 확인
        if (!payment.isOwner(userId)) {
            throw new Exception403("본인 결제만 환불 요청할 수 있습니다.");
        }
        // 결제 완료 상태만 환불 가능(READY, CANCELED는 환불 불가)
        if (!payment.isPaid()) {
            throw new Exception400("결제 완료된 건만 환불 요청할 수 있습니다.");
        }
        // 이미 환불 요청한 결제인지 확인(대기중, 승인됨, 거절됨 모두 신청 불가)
        if (refundRepository.findByPaymentPk(paymentPk).isPresent()) {
            throw new Exception400("이미 환불 요청한 결제입니다.");
        }
        return payment;
    }

    @Transactional
    public void save(RefundRequest.SaveDto saveDto, Long userId) {
        // 폼 요청 화면처럼 검사를 한 번 더 실행(주소 직접 요청 방지, 중복 제출 방지)
        Payment payment = checkRefundable(saveDto.getPaymentPk(), userId);
        // 환불 요청 저장
        Refund refund = Refund.builder().user(payment.getUser()).payment(payment).reason(saveDto.getReason()).build();
        refundRepository.save(refund);
    }

    // 내 환불 요청 내역(페이징)
    public PageResponse<RefundResponse.ListDto> findAllByUserId(Long userId, int page, int size) {
        Page<Refund> refundPage = refundRepository.findAllByUserIdJoinPayment(userId, PageResponse.pageable(page, size));
        List<RefundResponse.ListDto> dtoList = refundPage.getContent().stream().map(RefundResponse.ListDto::new).toList();
        return new PageResponse<>(refundPage, dtoList, "/refund/list?size=" + refundPage.getSize() + "&");
    }

    // 관리자 : 환불 요청 목록(페이징, status가 null이면 전체)
    // 조회만 하는 곳에 트랜잭션을 걸어줄 수 있다(readOnly) -> 정합성 때문(동시 접속해서 다른 사람이 내용을 수정하는 경우 대비)
    public PageResponse<RefundResponse.AdminListDto> findAllForAdmin(RefundStatus status, int page, int size) {
        Pageable pageable = PageResponse.pageable(page, size);
        Page<Refund> refundPage = (status == null)
                ? refundRepository.findAllJoinUserAndPayment(pageable)
                : refundRepository.findAllByStatusJoinUserAndPayment(status, pageable);
        List<RefundResponse.AdminListDto> dtoList = refundPage.getContent().stream().map(RefundResponse.AdminListDto::new).toList();

        // 상태 필터를 유지한 채로 페이지를 넘길 수 있도록 링크 앞부분에 status를 포함한다.
        String baseUrl = "/admin/refund/list?size=" + refundPage.getSize()
                + (status == null ? "" : "&status=" + status.name()) + "&";
        return new PageResponse<>(refundPage, dtoList, baseUrl);
    }

    // 관리자 대시보드 : 상태별 건수
    public RefundResponse.StatusCountDto countByStatus() {
        return new RefundResponse.StatusCountDto(
                refundRepository.countByStatus(RefundStatus.PENDING),
                refundRepository.countByStatus(RefundStatus.APPROVED),
                refundRepository.countByStatus(RefundStatus.REJECTED));
    }

    // 관리자 : 환불 요청 거절
    @Transactional
    public void reject(Long refundId, String rejectReason) {
        // 넘겨받은 refundId 기준으로 조회 -> 엔터티 정보를 업데이트 하기 위해
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new Exception404("환불 요청을 찾을 수 없습니다."));
        // 이미 승인, 거절된 요청은 다시 처리하지 않는다.
        if (!refund.isPending()) {
            throw new Exception400("대기 중인 환불 요청만 거절할 수 있습니다.");
        }
        refund.reject(rejectReason);    // 더티 체킹
    }

    // 관리자 승인 : 승인 가능 여부 확인
    public RefundResponse.ApproveDto checkApprove(Long refundId) {
        // 환불 엔터티
        Refund refund = refundRepository.findByIdJoinUserAndPayment(refundId)
                .orElseThrow(() -> new Exception404("환불 요청을 찾을 수 없습니다."));
        // 환불 상태 확인(대기, 승인, 거절) 중 대기 상태만 가능
        if (!refund.isPending()) {
            throw new Exception400("대기 중인 환불 요청만 승인할 수 있습니다.");
        }
        // 결제
        Payment payment = refund.getPayment();
        if (!payment.isPaid()) {
            throw new Exception400("결제 완료 상태만 환불 신청할 수 있습니다.");
        }
        // 환불 신청 시 포인트를 사용하여 부족한 경우
        User user = refund.getUser();
        if (user.getPoint() < payment.getAmount()) {
            throw new Exception400("포인트 잔액이 부족하여 환불할 수 없습니다.");
        }
        return new RefundResponse.ApproveDto(payment.getPaymentId(), payment.getAmount());
    }

    // 관리자 승인 - 포트원 결제 취소가 끝난 뒤 DB 반영
    @Transactional
    public void approve(Long refundId) {
        Refund refund = refundRepository.findByIdJoinUserAndPayment(refundId)
                .orElseThrow(() -> new Exception404("환불 요청을 찾을 수 없습니다."));
        // PortOneRefundService에 있는 approve의 1~4번 사이에 다른 요청이 같은 건을 처리했을 수 있으므로 한 번 더 확인
        if (!refund.isPending()) {
            throw new Exception400("대기 중인 환불 요청만 환불 승인 신청 가능");
        }
        Payment payment = refund.getPayment();
        refund.getUser().deductPoint(payment.getAmount());
        payment.markCancelled();    // 상태 값 변경 -> CANCELED
        refund.approve();
    }
}