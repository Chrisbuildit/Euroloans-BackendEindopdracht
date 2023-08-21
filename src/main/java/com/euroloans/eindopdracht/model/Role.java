package com.euroloans.eindopdracht.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    private String rolename;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
