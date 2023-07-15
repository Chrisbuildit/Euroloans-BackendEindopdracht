package com.euroloans.eindopdracht.model;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
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

    private String paymentReference;

    private Boolean allocated = false;

    //One-sided
    @ManyToOne
    private LoanRequest loanRequest;

    private Long loanId;

    private Long investmentId;

    @ManyToOne
    private User user;
}
