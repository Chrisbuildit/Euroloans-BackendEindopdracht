package com.euroloans.eindopdracht.model;

import jakarta.persistence.*;

@Entity
@Table(name="loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long loanId;

    @OneToOne
    LoanApplication loanApplication;



}
