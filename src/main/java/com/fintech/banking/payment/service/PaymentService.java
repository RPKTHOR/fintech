package com.fintech.banking.payment.service;

import com.fintech.banking.payment.dto.*;
import com.fintech.banking.payment.model.*;
import com.fintech.banking.payment.repository.PaymentRepository;
import com.fintech.banking.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final AccountService accountService;
    
    @Transactional
    public PaymentDTO initiateUpiPayment(UpiPaymentRequest request) {
        log.info("Initiating UPI payment to: {}", request.getUpiId());
        
        // Deduct from source account
        accountService.withdraw(request.getSourceAccountId(), request.getAmount(), 
            "UPI payment to " + request.getUpiId());
        
        Payment payment = Payment.builder()
            .sourceAccountId(request.getSourceAccountId())
            .destinationUpiId(request.getUpiId())
            .amount(request.getAmount())
            .paymentMode(PaymentMode.UPI)
            .description(request.getDescription())
            .beneficiaryName(request.getBeneficiaryName())
            .build();
        
        // Simulate payment processing
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setProcessedAt(java.time.LocalDateTime.now());
        
        Payment saved = paymentRepository.save(payment);
        return mapToDTO(saved);
    }
    
    @Transactional
    public PaymentDTO initiateNeftRtgs(NeftRtgsPaymentRequest request) {
        log.info("Initiating {} payment", request.getPaymentMode());
        
        // RTGS minimum validation
        if (request.getPaymentMode() == PaymentMode.RTGS && 
            request.getAmount().compareTo(new BigDecimal("200000")) < 0) {
            throw new RuntimeException("RTGS requires minimum Rs. 2,00,000");
        }
        
        accountService.withdraw(request.getSourceAccountId(), request.getAmount(),
            request.getPaymentMode() + " payment");
        
        Payment payment = Payment.builder()
            .sourceAccountId(request.getSourceAccountId())
            .destinationAccountNumber(request.getDestinationAccountNumber())
            .destinationIfsc(request.getIfscCode())
            .amount(request.getAmount())
            .paymentMode(request.getPaymentMode())
            .description(request.getDescription())
            .beneficiaryName(request.getBeneficiaryName())
            .status(PaymentStatus.SUCCESS)
            .processedAt(java.time.LocalDateTime.now())
            .build();
        
        Payment saved = paymentRepository.save(payment);
        return mapToDTO(saved);
    }
    
    public PaymentDTO getPayment(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
            .map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
    
    private PaymentDTO mapToDTO(Payment payment) {
        return PaymentDTO.builder()
            .id(payment.getId())
            .paymentId(payment.getPaymentId())
            .sourceAccountId(payment.getSourceAccountId())
            .destinationUpiId(payment.getDestinationUpiId())
            .destinationAccountNumber(payment.getDestinationAccountNumber())
            .amount(payment.getAmount())
            .paymentMode(payment.getPaymentMode())
            .status(payment.getStatus())
            .description(payment.getDescription())
            .beneficiaryName(payment.getBeneficiaryName())
            .createdAt(payment.getCreatedAt())
            .build();
    }
}
