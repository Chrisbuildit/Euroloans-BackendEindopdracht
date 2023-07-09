package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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

    private Integer balance;

    private Integer interest;

    private Double ROI;

    @OneToMany(mappedBy = "investment")
    @JsonIgnore
    private List<Payment> payments;

    @ManyToOne
    private Loan loan;

    @ManyToMany
    private Collection<User> users;

}
