package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.LoanApplicationDto;
import com.euroloans.eindopdracht.model.Loan;
import com.euroloans.eindopdracht.service.LoanApplicationService;
import com.euroloans.eindopdracht.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loanApplications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<LoanApplicationDto>> getAllLoanApplications() {
        return ResponseEntity.ok(loanApplicationService.getAllLoanApplications());
    }

    @PostMapping
    public ResponseEntity<Long> createLoanApplication(@RequestBody LoanApplicationDto loanApplicationDto) {
        return new ResponseEntity<>(loanApplicationService.createLoanApplication(loanApplicationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanApplicationDto> approveLoan(@PathVariable Long id, @RequestBody LoanApplicationDto loanApplicationDto) {
        LoanApplicationDto dto = loanApplicationService.approveLoan(id, loanApplicationDto);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLoanApplication(@PathVariable Long id) {
        return ResponseEntity.ok(loanApplicationService.getLoanApplication(id));
    }

}
