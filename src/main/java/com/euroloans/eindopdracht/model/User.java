package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    private String username;

    @Column(unique=true)
    private String password;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Payment> payments;

    @ManyToMany(mappedBy = "users")
    private Collection<LoanRequest> loanRequests;

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
}
