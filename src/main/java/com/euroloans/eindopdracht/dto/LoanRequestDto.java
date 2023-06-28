package com.euroloans.eindopdracht.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class LoanRequestDto {
    public Long id;

    public String name;
    public Boolean isApproved;

    public String usernameId;

    //    @Size(min=2)
    public List<String> usernameIds;
}
