package com.fintech.banking.account.controller;

import com.fintech.banking.account.dto.*;
import com.fintech.banking.account.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    @Operation(summary = "Create new account")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(accountService.createAccount(dto));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer accounts")
    public ResponseEntity<List<AccountDTO>> getCustomerAccounts(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getCustomerAccounts(customerId));
    }
    
    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit money")
    public ResponseEntity<TransactionDTO> deposit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        return ResponseEntity.ok(accountService.deposit(id, amount, description));
    }
    
    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw money")
    public ResponseEntity<TransactionDTO> withdraw(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        return ResponseEntity.ok(accountService.withdraw(id, amount, description));
    }

@GetMapping("/{id}/transactions")
    @Operation(summary = "Get account transaction history")
    public ResponseEntity<Page<TransactionDTO>> getTransactionHistory(
            @PathVariable Long id, Pageable pageable) {
        Page<TransactionDTO> transactions = accountService.getTransactionHistory(id, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}/balance")
    @Operation(summary = "Get account balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long id) {
        AccountDTO account = accountService.getAccount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", account.getAccountNumber());
        response.put("balance", account.getBalance());
        response.put("currency", account.getCurrency());
        return ResponseEntity.ok(response);
    }
}