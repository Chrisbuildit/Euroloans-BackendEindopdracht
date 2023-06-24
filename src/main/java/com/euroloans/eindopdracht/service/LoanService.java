package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.Loan;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanApplicationRepository;
import com.euroloans.eindopdracht.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    private final LoanApplicationRepository loanApplicationRepository;

    public LoanService(LoanRepository loanRepository, LoanApplicationRepository loanApplicationRepository) {
        this.loanRepository = loanRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    public String createLoan(LoanDto loanDto) {
        Loan newLoan = new Loan();
        LoanApplication loanapplication = loanApplicationRepository.findById(loanDto.loanApplicationId).get();

        if(loanapplication.isApproved == Boolean.TRUE) {
            newLoan.setLoanApplication(loanapplication);
            loanRepository.save(newLoan);

            return "Done";
        } else {
            return "The loanApplication first needs to be approved";
        }
    }

    public LoanDto getLoan(Long loanId) {
        Loan l = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("Loan not Found"));

        LoanDto loanDto = new LoanDto();
        loanDto.loanId = l.getLoanId();
        loanDto.loanApplicationId = l.getLoanApplication().getId();
        loanDto.loanApplicationName = l.getLoanApplication().getName();

        return loanDto;
    }
}
