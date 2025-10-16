package com.fintech.banking.account.service;

import com.fintech.banking.account.dto.*;
import com.fintech.banking.account.model.*;
import com.fintech.banking.account.repository.*;
import com.fintech.banking.common.event.BankingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, BankingEvent> kafkaTemplate;
    
    @Transactional
    public AccountDTO createAccount(AccountDTO dto) {
        log.info("Creating account for customer: {}", dto.getCustomerId());
        
        Account account = Account.builder()
            .customerId(dto.getCustomerId())
            .accountType(dto.getAccountType())
            .currency(dto.getCurrency())
            .interestRate(dto.getInterestRate())
            .branch(dto.getBranch())
            .build();
        
        Account saved = accountRepository.save(account);
        publishEvent("ACCOUNT_CREATED", saved);
        return mapToDTO(saved);
    }
    
    @Cacheable(value = "accounts", key = "#id")
    public AccountDTO getAccount(Long id) {
        return accountRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    
    public List<AccountDTO> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value = "accounts", key = "#accountId")
    public TransactionDTO deposit(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findByIdForUpdate(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        
        Transaction txn = createTransaction(account, TransactionType.CREDIT, amount, description);
        publishEvent("DEPOSIT_COMPLETED", account);
        
        return mapTransactionToDTO(txn);
    }
    
    @Transactional
    @CacheEvict(value = "accounts", key = "#accountId")
    public TransactionDTO withdraw(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findByIdForUpdate(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        
        Transaction txn = createTransaction(account, TransactionType.DEBIT, amount, description);
        publishEvent("WITHDRAWAL_COMPLETED", account);
        
        return mapTransactionToDTO(txn);
    }
    
    private Transaction createTransaction(Account account, TransactionType type, 
                                        BigDecimal amount, String description) {
        Transaction txn = Transaction.builder()
            .accountId(account.getId())
            .type(type)
            .amount(amount)
            .balanceAfter(account.getBalance())
            .description(description)
            .build();
        return transactionRepository.save(txn);
    }
    
    private void publishEvent(String eventType, Account account) {
        BankingEvent event = BankingEvent.builder()
            .eventType(eventType)
            .accountId(account.getId())
            .customerId(account.getCustomerId())
            .balance(account.getBalance())
            .build();
        kafkaTemplate.send("banking-events", event);
    }
    
    private AccountDTO mapToDTO(Account account) {
        return AccountDTO.builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .customerId(account.getCustomerId())
            .accountType(account.getAccountType())
            .balance(account.getBalance())
            .currency(account.getCurrency())
            .status(account.getStatus())
            .interestRate(account.getInterestRate())
            .branch(account.getBranch())
            .build();
    }
    
    private TransactionDTO mapTransactionToDTO(Transaction txn) {
        return TransactionDTO.builder()
            .id(txn.getId())
            .transactionId(txn.getTransactionId())
            .accountId(txn.getAccountId())
            .type(txn.getType())
            .amount(txn.getAmount())
            .balanceAfter(txn.getBalanceAfter())
            .description(txn.getDescription())
            .createdAt(txn.getCreatedAt())
            .build();
    }

    public Page<TransactionDTO> getTransactionHistory(Long accountId, Pageable pageable) {
        return transactionRepository.findByAccountId(accountId, pageable)
            .map(this::mapTransactionToDTO);
    }
    
}
