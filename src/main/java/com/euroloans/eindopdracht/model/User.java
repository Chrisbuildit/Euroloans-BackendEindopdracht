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
    private Long IdNumber;
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
    private List<Payment> payments;

    //Is mappedBy nodig?
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<LoanRequest> loanRequests;

    public Long getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(Long idNumber) {
        IdNumber = idNumber;
    }

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
