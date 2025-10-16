package com.fintech.banking.payment.repository;

import com.fintech.banking.payment.model.Payment;
import com.fintech.banking.payment.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    Page<Payment> findBySourceAccountId(Long sourceAccountId, Pageable pageable);
    List<Payment> findByStatus(PaymentStatus status);
}