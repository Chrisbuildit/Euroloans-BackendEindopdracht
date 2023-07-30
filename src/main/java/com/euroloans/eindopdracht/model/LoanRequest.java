package com.euroloans.eindopdracht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
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

    @ManyToMany
    @JoinTable(name = "loanRequest_user_mapping",
        joinColumns = {@JoinColumn(name = "loanRequests_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "id")})
//    @MapKeyJoinColumn(name = "roles_id")
    @MapKey(name = "username")
    public Map<String, User> users = new HashMap<>();

    @JsonIgnore
    @OneToOne
    private File file;

    public void addUsers(String string, User user) {
        users.put(string,user);
    }

}
