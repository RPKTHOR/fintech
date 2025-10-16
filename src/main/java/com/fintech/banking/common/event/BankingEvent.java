package com.fintech.banking.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import jakarta.persistence.PrePersist;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankingEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String eventType;
    private Long accountId;
    private String accountNumber;
    private Long customerId;
    private BigDecimal balance;
    private BigDecimal amount;
    private String paymentId;
    private String transactionId;
    private String status;
    private Long timestamp;
    
    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
    }
}
