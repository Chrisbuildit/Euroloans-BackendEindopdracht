package com.euroloans.eindopdracht.dto;

import lombok.Value;

import java.time.LocalDate;

public class PaymentDto {
    public Long paymentId;

    public LocalDate date;

    public Integer amount;

    public String paymentReference;

    public Boolean allocated;

    public Long loanRequestId;

    public String usernameId;

    public Long investmentId;

    public Long loanId;
    
}
