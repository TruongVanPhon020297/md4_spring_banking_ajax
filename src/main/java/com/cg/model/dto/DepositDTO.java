package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class DepositDTO {
    private String id;
    
    @NotBlank(message = "Customer Id í required")
    @Pattern(regexp = "^[0-9]+$", message = "Customer ID only digit")
    private String customerId;
    
    @NotNull(message = "Not Null")
    @Length(min = 1,max = 7,message = "50 --> 10000")
    @Pattern(regexp = "^[0-9]+$", message = "Transaction only digit")
    @DecimalMax(message = "max 10000", value = "10000")
    @DecimalMin(message = "min 50", value = "50")
    private String transactionAmount;


    public Deposit toDeposit(Customer customer) {
        return new Deposit()
                .setId(0L)
                .setTransactionAmount(new BigDecimal(Long.parseLong(transactionAmount)))
                .setCustomer(customer);
    }
}
