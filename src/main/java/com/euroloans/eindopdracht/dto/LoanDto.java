package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Investment;
import com.euroloans.eindopdracht.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanDto {

    public Long loanId;

    public Long loanRequestId;

    public Integer balance;

    public List<Investment> investments;

    public Collection<User> usernameIds;

}
