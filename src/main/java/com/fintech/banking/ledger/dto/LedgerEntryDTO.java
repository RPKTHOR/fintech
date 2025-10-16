package com.fintech.banking.ledger.dto;

import com.fintech.banking.ledger.model.AccountHead;
import com.fintech.banking.ledger.model.EntryStatus;
import com.fintech.banking.ledger.model.EntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class LedgerEntryDTO {
    private Long id;
    private String entryId;
    private String transactionId;
    
    @NotNull(message = "Account ID is required")
    private Long accountId;
    
    @NotNull(message = "Entry type is required")
    private EntryType entryType;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private String currency;
    
    @NotNull(message = "Account head is required")
    private AccountHead accountHead;
    
    private String description;
    private String referenceNumber;
    private EntryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime postedAt;
}
