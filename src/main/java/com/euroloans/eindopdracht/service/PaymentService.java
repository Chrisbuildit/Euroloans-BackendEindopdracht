package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.PaymentDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.InvestmentRepository;
import com.euroloans.eindopdracht.repository.LoanRepository;
import com.euroloans.eindopdracht.repository.PaymentRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final LoanRepository loanRepository;

    private final InvestmentRepository investmentRepository;

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository, LoanRepository loanRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.investmentRepository = investmentRepository;
    }

    public Long createPayment(PaymentDto paymentDto) {

        Payment payment = new Payment();
        payment.setDate(java.time.LocalDate.now());
        payment.setAmount(paymentDto.amount);
        payment.setPaymentReference(paymentDto.paymentReference);

        User user = userRepository.findById(paymentDto.usernameId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found"));
        payment.setUser(user);

        Role role = user.getRole();
        if (role.getRolename().equals("ROLE_BORROWER")) {
            Loan loan = loanRepository.findById(paymentDto.loanId).orElseThrow(() ->
                    new ResourceNotFoundException("Loan not Found"));
            payment.setLoan(loan);
        } else {
            Investment investment = investmentRepository.findById(paymentDto.investmentId).orElseThrow(() ->
                    new ResourceNotFoundException("Investment not Found"));
            payment.setInvestment(investment);
        }

        paymentRepository.save(payment);

        return payment.getPaymentId();
    }

    public PaymentDto getPayment(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Loan not Found"));

        return transferToDto(p);
    }

    public List<PaymentDto> getAllPayments() {
        Iterable<Payment> pList = paymentRepository.findAll();
        List<PaymentDto> pDtoList = new ArrayList<>();

        for(Payment p : pList) {
            PaymentDto paymentDto = transferToDto(p);
            pDtoList.add(paymentDto);
        }
        return pDtoList;
    }

    public PaymentDto transferToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.paymentId = payment.getPaymentId();
        paymentDto.date = payment.getDate();
        paymentDto.amount = payment.getAmount();
        paymentDto.usernameId = payment.getUser().getUsername();
        paymentDto.paymentReference = payment.getPaymentReference();

        if(payment.getLoan() != null) {
            paymentDto.loanId = payment.getLoan().getLoanId();
        }
        if (payment.getInvestment() != null) {
            paymentDto.investmentId = payment.getInvestment().getInvestmentId();
        }

        return paymentDto;
    }

}
