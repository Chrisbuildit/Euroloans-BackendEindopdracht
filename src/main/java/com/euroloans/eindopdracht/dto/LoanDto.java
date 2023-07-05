package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanDto {

    public Long loanId;

    public Long loanRequestId;

    public String loanRequestName;

    public Map<String, User> usernameIds;
    //    public List<String> usernameIds;

}
