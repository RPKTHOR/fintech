package com.fintech.banking.ledger.service;

import com.fintech.banking.ledger.dto.LedgerEntryDTO;
import com.fintech.banking.ledger.model.*;
import com.fintech.banking.ledger.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// COMMENT OUT THIS IMPORT:
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.kafka.core.KafkaTemplate;
// import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LedgerService {
    
    private final LedgerEntryRepository ledgerEntryRepository;
    // COMMENT OUT:
    // private final KafkaTemplate<String, String> kafkaTemplate;
    // private final ObjectMapper objectMapper;
    
    // COMMENT OUT THIS ENTIRE METHOD:
    /*
    @KafkaListener(topics = "banking-events", groupId = "ledger-service-group")
    @Transactional
    public void processAccountEvent(String eventJson) {
        log.info("Processing banking event for ledger: {}", eventJson);
        
        try {
            BankingEvent event = objectMapper.readValue(eventJson, BankingEvent.class);
            
            if ("DEPOSIT_COMPLETED".equals(event.getEventType())) {
                createDepositJournalEntry(event);
            } else if ("WITHDRAWAL_COMPLETED".equals(event.getEventType())) {
                createWithdrawalJournalEntry(event);
            }
            
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage());
        }
    }
    
    @Transactional
    public void createDepositJournalEntry(BankingEvent event) {
        // ... commented out
    }
    
    @Transactional
    public void createWithdrawalJournalEntry(BankingEvent event) {
        // ... commented out
    }
    */
    
    // KEEP THESE METHODS (they still work):
    
    public List<LedgerEntryDTO> getEntriesByAccount(Long accountId) {
        log.info("Fetching ledger entries for account: {}", accountId);
        return ledgerEntryRepository.findByAccountId(accountId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public List<LedgerEntryDTO> getEntriesByTransaction(String transactionId) {
        log.info("Fetching ledger entries for transaction: {}", transactionId);
        return ledgerEntryRepository.findByTransactionId(transactionId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public BigDecimal getAccountBalance(Long accountId) {
        log.info("Calculating ledger balance for account: {}", accountId);
        
        BigDecimal totalDebits = ledgerEntryRepository
            .sumByAccountIdAndEntryType(accountId, EntryType.DEBIT);
        
        BigDecimal totalCredits = ledgerEntryRepository
            .sumByAccountIdAndEntryType(accountId, EntryType.CREDIT);
        
        if (totalDebits == null) totalDebits = BigDecimal.ZERO;
        if (totalCredits == null) totalCredits = BigDecimal.ZERO;
        
        return totalDebits.subtract(totalCredits);
    }
    
    private LedgerEntryDTO mapToDTO(LedgerEntry entry) {
        return LedgerEntryDTO.builder()
            .id(entry.getId())
            .entryId(entry.getEntryId())
            .transactionId(entry.getTransactionId())
            .accountId(entry.getAccountId())
            .entryType(entry.getEntryType())
            .amount(entry.getAmount())
           // .currency(entry.getCurrency())
            .accountHead(entry.getAccountHead())
            .description(entry.getDescription())
           // .referenceNumber(entry.getReferenceNumber())
            .status(entry.getStatus())
            .createdAt(entry.getCreatedAt())
          //  .postedAt(entry.getPostedAt())
            .build();
    }
}