package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.Investment;
import com.euroloans.eindopdracht.model.LoanRequest;
import org.springframework.data.repository.CrudRepository;

public interface InvestmentRepository extends CrudRepository<Investment, Long> {
}
