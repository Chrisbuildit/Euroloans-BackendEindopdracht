package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Investment;
import com.euroloans.eindopdracht.model.Payment;
import com.euroloans.eindopdracht.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;

import java.util.Collection;
import java.util.List;

public class InvestmentDto {

    public Long investmentId;

    public Integer balance;

    public List<Long> paymentList;

    public Long loanRequestId;

    public Long loanId;


    public String usernameId;



}
