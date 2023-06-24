package com.euroloans.eindopdracht.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;

@Entity
@Table(name="loanApplications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id ;

    public String name;

    @Value("false")
    public Boolean isApproved;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }
}
