package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="loanRequests")
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    private String name;

    private Integer amount;

    @Transient
    private String usernameId;

    @Value("false")
    public Boolean isApproved;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "loanRequest_user_mapping",
        joinColumns = {@JoinColumn(name = "loanRequests_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "id")})
//    @MapKeyJoinColumn(name = "roles_id")
    @MapKey(name = "username")
    public Map<String, User> users = new HashMap<>();

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

    public void addUsers(String string, User user) {
        users.put(string,user);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}
