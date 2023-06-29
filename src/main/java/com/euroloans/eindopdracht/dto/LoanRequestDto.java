package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;

public class LoanRequestDto {
    public Long id;

    public String name;

    public Integer amount;
    public Boolean isApproved;

    public String usernameId;

    //    @Size(min=2)
//    public List<String> usernameIds;
    public HashMap<String, User> usernameIds;
}
