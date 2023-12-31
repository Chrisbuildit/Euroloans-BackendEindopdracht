package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDto {

    public String username;


    public String password;

    public String rolenameId;


    public List<Long> investments;

    public List<Long> loans;

    public List<String> loanRequests;


    public String getUsername() {
        return username;
    }

    public String getRolenameId() {
        return rolenameId;
    }
}
