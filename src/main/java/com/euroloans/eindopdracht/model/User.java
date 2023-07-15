package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    //Werk nie
    @Column(unique=true, name = "id")
    private String username;

    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "roles_id")
    public Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Payment> payments;

    //Is mappedBy nodig hier?
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<LoanRequest> loanRequests;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Investment> investments;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private Collection<Loan> loans;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<LoanRequest> getLoanRequests() {
        return loanRequests;
    }

    public void setLoanRequests(Collection<LoanRequest> loanRequests) {
        this.loanRequests = loanRequests;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }

    public Collection<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(Collection<Investment> investments) {
        this.investments = investments;
    }

    public Collection<Loan> getLoans() {
        return loans;
    }

    public void setLoans(Collection<Loan> loans) {
        this.loans = loans;
    }
}
