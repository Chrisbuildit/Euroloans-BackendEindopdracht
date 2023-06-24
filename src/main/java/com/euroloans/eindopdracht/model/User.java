package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    public String username;

    @Column(unique=true)
    private String password;

    @OneToOne
    private Role role;

    @ManyToMany(mappedBy = "users")
    private Collection<LoanApplication> loanApplications;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(Collection<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
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
