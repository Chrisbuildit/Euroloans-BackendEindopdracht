package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRequestRepository extends CrudRepository<LoanRequest,Long> {

}
