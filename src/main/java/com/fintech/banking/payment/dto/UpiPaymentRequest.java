package com.fintech.banking.payment.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpiPaymentRequest {
    @NotNull(message = "Source account ID is required")
    private Long sourceAccountId;
    
    @NotBlank(message = "UPI ID is required")
    @Pattern(regexp = "^[\\w.+-]+@[\\w.-]+$", message = "Invalid UPI ID format")
    private String upiId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private String description;
    private String beneficiaryName;
}