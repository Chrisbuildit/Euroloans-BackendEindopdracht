package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.service.LoanRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/loanRequests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLoanRequest(@PathVariable Long id) {
        return ResponseEntity.ok(loanRequestService.getLoanRequest(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequests() {
        return ResponseEntity.ok(loanRequestService.getAllLoanRequests());
    }

    @PostMapping
    public ResponseEntity<LoanRequest> createLoanRequest(@RequestBody LoanRequestDto loanRequestDto) {
        return new ResponseEntity<>(loanRequestService.createLoanRequest(loanRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanRequest> updateLoan(@PathVariable Long id, @RequestBody LoanRequestDto loanRequestDto) {
        return new ResponseEntity<>(loanRequestService.updateLoan(id, loanRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable Long id) {
        loanRequestService.deleteLoanRequest(id);
        return ResponseEntity.noContent().build();
    }
}
