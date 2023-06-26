package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanApplicationDto;
import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.Loan;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanApplicationRepository;
import com.euroloans.eindopdracht.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        loanDto.loanApplicationId = loan.getLoanApplication().getId();
        loanDto.loanApplicationName = loan.getLoanApplication().getName();

        List<String> usernames = new ArrayList<>();
        for (User u : loan.getLoanApplication().getUsers()) {
            usernames.add(u.getUsername());
        }
        loanDto.usernameId = usernames;

        return loanDto;
    }
}
