package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.service.LoanRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loanRequests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    @GetMapping
    public ResponseEntity<List<LoanRequestDto>> getAllLoanRequests() {
        return ResponseEntity.ok(loanRequestService.getAllLoanRequests());
    }

    @PostMapping
    public ResponseEntity<Long> createLoanRequest(@RequestBody LoanRequestDto loanRequestDto) {
        return new ResponseEntity<>(loanRequestService.createLoanRequest(loanRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> approveLoan(@PathVariable Long id, @RequestBody LoanRequestDto loanRequestDto) {
        return new ResponseEntity<>(loanRequestService.approveLoan(id, loanRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLoanRequest(@PathVariable Long id) {
        return ResponseEntity.ok(loanRequestService.getLoanRequest(id));
    }
}
