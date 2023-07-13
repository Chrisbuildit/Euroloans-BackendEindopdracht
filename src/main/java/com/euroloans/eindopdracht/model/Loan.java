package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    private Integer balance;

    @OneToOne
    LoanRequest loanRequest;

    //One-sided dependency
    @OneToMany
    private List<Payment> payments;

    @OneToMany(mappedBy = "loan")
    @JsonIgnore
    private List<Investment> investments;


    @ManyToOne
    private User createdBy;

    public Integer increaseBalance(Integer i) {
        balance = balance + i;
        return balance;
    }

    public Integer decreaseBalance(Integer i) {
        balance = balance - i;
        return balance;
    }
}
