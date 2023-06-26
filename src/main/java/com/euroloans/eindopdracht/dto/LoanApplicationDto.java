package com.euroloans.eindopdracht.dto;

//Gee error!
//import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class LoanApplicationDto {
    public Long id;

    public String name;
    public Boolean isApproved;

//    @Size(min=2)
    public List<String> usernameId;
}
