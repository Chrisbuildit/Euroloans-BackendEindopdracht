package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.InvestmentRepository;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.LoanRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    private final LoanRequestRepository loanRequestRepository;

    private final UserRepository userRepository;

    private final InvestmentRepository investmentRepository;

    public LoanService(LoanRepository loanRepository, LoanRequestRepository loanRequestRepository, UserRepository userRepository, InvestmentRepository investmentRepository) {
        this.loanRepository = loanRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
        this.investmentRepository = investmentRepository;
    }

    public Loan createLoan(LoanDto loanDto) {
        Loan newLoan = new Loan();
        LoanRequest loanRequest = loanRequestRepository.findById(loanDto.loanRequestId).orElseThrow(() ->
                new ResourceNotFoundException("LoanRequest not Found"));

        UserIdentification userIdentification = new UserIdentification(userRepository);
        User user = userIdentification.getCurrentUser();

        Iterable<Investment> investments = investmentRepository.findByLoanRequestId(loanRequest.getId());
        int totalInvestmentBalance = 0;
        for (Investment investment : investments) {
            totalInvestmentBalance += investment.getBalance();
        }

        if(loanRequest.isApproved == Boolean.TRUE) {
        if(totalInvestmentBalance==loanRequest.getAmount()) {
            newLoan.setLoanRequest(loanRequest);
            newLoan.setBalance(loanRequest.getAmount());
            newLoan.setCreatedBy(user);
            for (Investment investment : investments) {
                //Example of implementing One-To-Many relationship. The many side contains the one-side
                investment.setLoan(newLoan);
            }
            loanRepository.save(newLoan);

            return newLoan;
        } else {
            throw new ResourceNotFoundException("The available funding does not match the loanAmount requested");
        }
        } else {
            throw new ResourceNotFoundException("The loanRequest first needs to be approved");
        }
    }

    public LoanDto getLoan(Long loanId) {
        Loan l = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("Loan not Found"));

        return transferToDto(l);
    }

    public List<LoanDto> getAllLoans() {
        Iterable<Loan> lList = loanRepository.findAll();
        List<LoanDto> lDtoList = new ArrayList<>();

        for(Loan l : lList) {
            LoanDto loanDto = transferToDto(l);
            lDtoList.add(loanDto);
        }
        return lDtoList;
    }

    public LoanDto transferToDto(Loan loan) {
        LoanDto loanDto = new LoanDto();
        loanDto.loanId = loan.getLoanId();
        loanDto.loanRequestId = loan.getLoanRequest().getId();
        loanDto.loanRequestName = loan.getLoanRequest().getName();
        loanDto.usernameIds = loan.getLoanRequest().getUsers();
        loanDto.createdBy = loan.getCreatedBy().getUsername();
        loanDto.balance = loan.getBalance();
        loanDto.investments = loan.getInvestments();

        return loanDto;
    }
}
