package com.euroloans.eindopdracht.dto;

import com.euroloans.eindopdracht.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class UserDto {

    public String username;

    public String password;

    public String rolenameId;

    public List<String> loanRequests;
}
