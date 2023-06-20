package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
}
