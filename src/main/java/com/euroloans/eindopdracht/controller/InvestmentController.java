package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.InvestmentDto;
import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.PaymentDto;
import com.euroloans.eindopdracht.model.Investment;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.service.InvestmentService;
import com.euroloans.eindopdracht.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    public ResponseEntity<Investment> createInvestment(@RequestBody InvestmentDto investmentDto) {
        return new ResponseEntity<>(investmentService.createInvestment(investmentDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getInvestment(@PathVariable Long id) {
        return ResponseEntity.ok(investmentService.getInvestment(id));
    }

    @GetMapping
    public ResponseEntity<List<InvestmentDto>> getAllInvestments() {
        return ResponseEntity.ok(investmentService.getAllInvestments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable Long id) {
        return new ResponseEntity<>(investmentService.adjustBalance(id), HttpStatus.CREATED);
    }
}
