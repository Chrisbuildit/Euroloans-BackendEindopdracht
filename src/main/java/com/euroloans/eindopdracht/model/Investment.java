package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="investments")
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long investmentId;

    private Integer balance = 0;

    private Integer interest;

    private Double ROI;

    private Long loanRequestId;

    @Transient
    private String usernameId;

    //One-sided dependency
    @OneToMany
    private List<Payment> payments;

    @ManyToOne
    private Loan loan;

    @ManyToMany
    private Collection<User> users = new ArrayList<>();

    public void addUsers(User user) {
        users.add(user);
    }

    public Integer increaseBalance(Integer i) {
        balance = balance + i;
        return balance;
    }

    public Integer decreaseBalance(Integer i) {
        balance = balance - i;
        return balance;
    }

}
