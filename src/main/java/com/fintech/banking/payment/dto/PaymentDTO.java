package com.fintech.banking.payment.dto;

import com.fintech.banking.payment.model.PaymentMode;
import com.fintech.banking.payment.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String paymentId;
    private Long sourceAccountId;
    private String destinationUpiId;
    private String destinationAccountNumber;
    private String destinationIfsc;
    private BigDecimal amount;
    private String currency;
    private PaymentMode paymentMode;
    private PaymentStatus status;
    private String description;
    private String beneficiaryName;
    private LocalDateTime processedAt;
    private String failureReason;
    private LocalDateTime createdAt;
}