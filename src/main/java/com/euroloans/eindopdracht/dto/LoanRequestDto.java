package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanRequestDto {

    public Long id;

    public String name;
    public Integer amount;
    public Boolean isApproved;
    public String usernameId;

    //    @Size(min=2)
//    public List<String> usernameIds;
    public Map<String, Map<String, String>> usernameIds;

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }
}
