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
    public Long fileId;

    public String usernameId;

    public Map<String, User> users;

    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }
}
