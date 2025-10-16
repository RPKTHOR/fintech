package com.fintech.banking.ledger.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String entryId;
    
    @Column(nullable = false)
    private String transactionId;
    
    @Column(nullable = false)
    private Long accountId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType; // DEBIT or CREDIT
    
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private AccountHead accountHead;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private EntryStatus status = EntryStatus.POSTED;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        if (entryId == null) {
            entryId = "LE" + System.currentTimeMillis();
        }
    }
}
