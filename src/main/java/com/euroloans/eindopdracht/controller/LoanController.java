package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("loans")
    public ResponseEntity<String> createLoan(@RequestBody LoanDto loanDto) {
        return new ResponseEntity<>(loanService.createLoan(loanDto), HttpStatus.CREATED);
    }

    @GetMapping("/loan")
    public ResponseEntity<Object> getUser(@RequestParam Long loanId) {
        return ResponseEntity.ok(loanService.getLoan(loanId));
    }
}
