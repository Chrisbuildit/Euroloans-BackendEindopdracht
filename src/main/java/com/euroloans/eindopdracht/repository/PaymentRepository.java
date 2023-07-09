package com.euroloans.eindopdracht.repository;

import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
