package com.euroloans.eindopdracht.dto;

import java.time.LocalDate;

public class PaymentDto {
    public Long paymentId;

    public LocalDate date;

    public Integer amount;

    public String paymentReference;
    
    public Long loanId;
    
    public Long investmentId;
    
    public String usernameId;
    
}
