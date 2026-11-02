package com.tenco.spring_blog_practice.refund;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    // 결제(payment.id)로 조회 기능 추가
    @Query("select r from Refund r where r.payment.id = :paymentPk")
    Optional<Refund> findByPaymentPk(@Param("paymentPk") Long paymentPk);

    // 사용자가 환불 요청한 결제 PK 목록(결제 내역 화면에 환불 버튼 표시로 사용)
    @Query("select r.payment.id from Refund r where r.user.id = :userId")
    List<Long> findPaymentPkListByUserId(@Param("userId") Long userId);

    // 로그인한 사용자의 환불 요청 내역 (결제 정보를 함께 조회, 최신 순으로 정렬, 페이징)
    @Query(value = """
            select r from Refund r
            join fetch r.payment
            where r.user.id = :userId
            order by r.id desc
            """,
            countQuery = """
            select count(r) from Refund r
            where r.user.id = :userId
            """)
    Page<Refund> findAllByUserIdJoinPayment(@Param("userId") Long userId, Pageable pageable);

    // 관리자 - 전체 환불 요청 목록(페이징)
    @Query(value = """
            select r from Refund r
            join fetch r.user
            join fetch r.payment
            order by r.id desc
            """,
            countQuery = "select count(r) from Refund r")
    Page<Refund> findAllJoinUserAndPayment(Pageable pageable);

    // 관리자 - 상태별(대기, 승인, 거절) 환불 요청 목록(페이징)
    @Query(value = """
            select r from Refund r
            join fetch r.user
            join fetch r.payment
            where r.status = :status
            order by r.id desc
            """,
            countQuery = "select count(r) from Refund r where r.status = :status")
    Page<Refund> findAllByStatusJoinUserAndPayment(@Param("status") RefundStatus status, Pageable pageable);

    // 관리자 대시보드 - 상태별 환불 요청 건수
    @Query("select count(r) from Refund r where r.status = :status")
    long countByStatus(@Param("status") RefundStatus status);

    // 관리자 - 승인할 환불 요청 1건 조회
    @Query("""
            select r from Refund r
            join fetch r.user
            join fetch r.payment
            where r.id = :id
            """)
    Optional<Refund> findByIdJoinUserAndPayment(@Param("id") Long id);
}
