package com.euroloans.eindopdracht.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private LocalDate date;

    private Integer amount;

    @ManyToOne
    private Loan loan;

    @ManyToOne
    private Investment investment;

    @ManyToOne
    private User user;
}
