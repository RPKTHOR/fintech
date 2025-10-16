package com.fintech.banking.payment.dto;


import com.fintech.banking.payment.model.PaymentMode;
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
public class NeftRtgsPaymentRequest {
    @NotNull(message = "Source account ID is required")
    private Long sourceAccountId;
    
    @NotBlank(message = "Destination account number is required")
    private String destinationAccountNumber;
    
    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    private String ifscCode;
    
    @NotBlank(message = "Beneficiary name is required")
    private String beneficiaryName;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;
    
    private String description;
}
