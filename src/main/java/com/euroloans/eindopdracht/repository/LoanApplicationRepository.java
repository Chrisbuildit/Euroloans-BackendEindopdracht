package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.LoanApplication;
import org.springframework.data.repository.CrudRepository;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication,Long> {
}
