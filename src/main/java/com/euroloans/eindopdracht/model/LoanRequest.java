package com.euroloans.eindopdracht.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.HashMap;

@NoArgsConstructor
@Entity
@Table(name="loanRequests")
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name;

    private Integer amount;

    private String usernameId;

    @Value("false")
    public Boolean isApproved;

    @ManyToMany(fetch = FetchType.EAGER)
//    public Collection<User> users;
    public HashMap<String, User> users;

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

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(String usernameId) {
        this.usernameId = usernameId;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public void addUsers(String string, User user) {
        users.put(string,user);
    }
}
