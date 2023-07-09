package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Investment;
import com.euroloans.eindopdracht.model.Payment;
import com.euroloans.eindopdracht.model.User;

import java.util.Collection;
import java.util.List;

public class InvestmentDto {

    public Long investmentId;

    public Integer balance;

    public Integer interest;

    public Double ROI;

    public List<Payment> payments;

    public Long LoanId;

    public Collection<User> users;
}
