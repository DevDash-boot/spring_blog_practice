package com.tenco.spring_blog_practice.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.paymentId = :paymentId")
    Optional<Payment> findByPaymentId(@Param("paymentId") String paymentId);

    @Query("""
            select case when count(p) > 0 then true else false end
            from Payment p
            where p.paymentId = :paymentId
            """)
    boolean existsByPaymentId(@Param("paymentId") String paymentId);

    @Query("""
            select p from Payment p
            where p.user.id = :userId
            order by p.id desc
            """)
    List<Payment> findAllByUserId(@Param("userId") Long userId);

    // 결제 내역 화면용 : 보여줄 상태만 골라서 최신 순으로 페이징 조회
    // countQuery : 전체 개수 조회(페이지 수 계산용)
    @Query(value = """
            select p from Payment p
            where p.user.id = :userId
            and p.status in :statusList
            order by p.id desc
            """,
            countQuery = """
            select count(p) from Payment p
            where p.user.id = :userId
            and p.status in :statusList
            """)
    Page<Payment> findAllByUserIdAndStatusList(@Param("userId") Long userId,
                                               @Param("statusList") List<PaymentStatus> statusList,
                                               Pageable pageable);
}
