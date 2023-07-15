package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Loan;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.List;

public class UserDto {

    public String username;

    public String password;

    public String rolenameId;

    public Collection<Loan> loans;

    public List<String> loanRequests;
}
