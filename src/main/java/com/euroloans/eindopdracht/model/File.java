package com.euroloans.eindopdracht.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
}
