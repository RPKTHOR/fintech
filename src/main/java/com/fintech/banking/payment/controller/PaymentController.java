package com.fintech.banking.payment.controller;

import com.fintech.banking.payment.dto.*;
import com.fintech.banking.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Processing")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/upi")
    @Operation(summary = "Initiate UPI payment")
    public ResponseEntity<PaymentDTO> initiateUpiPayment(
            @Valid @RequestBody UpiPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(paymentService.initiateUpiPayment(request));
    }
    
    @PostMapping("/neft-rtgs")
    @Operation(summary = "Initiate NEFT/RTGS payment")
    public ResponseEntity<PaymentDTO> initiateNeftRtgs(
            @Valid @RequestBody NeftRtgsPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(paymentService.initiateNeftRtgs(request));
    }
    
    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}