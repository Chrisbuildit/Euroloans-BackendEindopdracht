package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.Loan;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.User;
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

    public LoanService(LoanRepository loanRepository, LoanRequestRepository loanRequestRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.userRepository = userRepository;
    }

    public Loan createLoan(LoanDto loanDto) {
        Loan newLoan = new Loan();
        LoanRequest loanRequest = loanRequestRepository.findById(loanDto.loanRequestId).orElseThrow(() ->
                new ResourceNotFoundException("LoanRequest not Found"));

        UserIdentification userIdentification = new UserIdentification(userRepository);
        User user = userIdentification.getCurrentUser();
        //Temporary disabled
//        if(loanRequest.isApproved == Boolean.TRUE) {
            newLoan.setLoanRequest(loanRequest);
            newLoan.setCreatedBy(user);
            loanRepository.save(newLoan);

            return newLoan;
        //Temporary disabled
//        } else {
//            throw new ResourceNotFoundException("The loanRequest first needs to be approved");
//        }
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
        loanDto.usernameId = loan.getCreatedBy().getUsername();

        return loanDto;
    }
}
