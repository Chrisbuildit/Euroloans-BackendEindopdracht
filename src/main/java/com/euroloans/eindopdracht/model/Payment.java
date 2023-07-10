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

    @ManyToOne
    private LoanRequest loanRequest;

    //One-sided dependency
    @ManyToOne
    private Loan loan;

//    @ManyToOne
//    private Investment investment;

    @ManyToOne
    private User user;
}
