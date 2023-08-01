package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "id")
    private String username;

    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "roles_id")
    private Role role;

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

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Loan> loans;

}
