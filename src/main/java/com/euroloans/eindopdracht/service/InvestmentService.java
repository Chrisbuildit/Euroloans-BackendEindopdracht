package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.InvestmentDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.InvestmentRepository;
import com.euroloans.eindopdracht.repository.PaymentRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final InvestmentRepository investmentRepository;

    public InvestmentService(UserRepository userRepository, PaymentRepository paymentRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.investmentRepository = investmentRepository;
    }

    public Investment createInvestment(InvestmentDto investmentDto) {
        Investment investment = new Investment();

        User employee = userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName()).
                orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

        User lender = userRepository.findById(investmentDto.usernameId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found"));

        List<Payment> investmentPayment = new ArrayList<>();
        for (Long paymentId : investmentDto.paymentList) {
            Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("PaymentId not found"));
            if (payment.getUser().getRole().getRolename().equals("ROLE_LENDER") && payment.getAllocated().equals(false)) {
                investmentPayment.add(payment);
                investment.increaseBalance(payment.getAmount());
                investment.setLoanRequestId(payment.getLoanRequest().getId());
                investment.addUsers(lender);
                investment.addUsers(employee);
                payment.setAllocated(true);
                payment.setInvestmentId(investment.getInvestmentId());

                investment.setPayments(investmentPayment);
                investmentRepository.save(investment);
            }
        }
            return investment;
    }

    public InvestmentDto getInvestment(Long investmentId) {
        Investment i = investmentRepository.findById(investmentId).orElseThrow(() -> new ResourceNotFoundException("Investment not Found"));

        return transferToDto(i);
    }

    public List<InvestmentDto> getAllInvestments() {
        Iterable<Investment> iList = investmentRepository.findAll();
        List<InvestmentDto> iDtoList = new ArrayList<>();

        for(Investment i : iList) {
            InvestmentDto investmentDto = transferToDto(i);
            iDtoList.add(investmentDto);
        }
        return iDtoList;
    }

    public InvestmentDto transferToDto(Investment investment) {
        InvestmentDto investmentDto = new InvestmentDto();
        investmentDto.investmentId = investment.getInvestmentId();
        investmentDto.balance = investment.getBalance();
        investmentDto.loanRequestId = investment.getLoanRequestId();

        for (User user : investment.getUsers()) {
            investmentDto.usernameId = user.getUsername();
        }

        List<Long> payments = new ArrayList<>();
        for (Payment payment : investment.getPayments()) {
            payments.add(payment.getPaymentId());
        }
        investmentDto.paymentList = payments;

        if(investment.getLoans() != null) {
            investmentDto.loanId = investment.getLoans().getLoanId();
        }

        return investmentDto;
        }
    }