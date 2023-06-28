package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.LoanRequest;
import org.springframework.data.repository.CrudRepository;

public interface LoanRequestRepository extends CrudRepository<LoanRequest,Long> {
}
