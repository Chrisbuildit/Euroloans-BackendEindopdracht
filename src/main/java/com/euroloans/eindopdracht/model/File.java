package com.euroloans.eindopdracht.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class File {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;
    @Lob
    private byte[] docfile;

    @OneToOne
    private User user;
}
