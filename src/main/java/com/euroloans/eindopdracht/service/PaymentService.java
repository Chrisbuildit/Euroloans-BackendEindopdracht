package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanDto;
import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.PaymentDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final LoanRepository loanRepository;

    private final LoanRequestRepository loanRequestRepository;

    private final InvestmentRepository investmentRepository;

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository, LoanRepository loanRepository, LoanRequestRepository loanRequestRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.investmentRepository = investmentRepository;
    }

    public Payment createPayment(PaymentDto paymentDto) {

        Payment payment = new Payment();
        payment.setDate(java.time.LocalDate.now());
        payment.setAmount(paymentDto.amount);
        payment.setPaymentReference(paymentDto.paymentReference);

        UserIdentification userIdentification = new UserIdentification(userRepository);
        User user = userIdentification.getCurrentUser();
        payment.setUser(user);

        Role role = user.getRole();
        if (role.getRolename().equals("ROLE_BORROWER")) {
            Loan loan = loanRepository.findById(paymentDto.loanId).orElseThrow(() ->
                    new ResourceNotFoundException("Loan not Found"));
            payment.setLoan(loan);
        } else {
            LoanRequest loanRequest = loanRequestRepository.findById(paymentDto.loanRequestId).orElseThrow(() ->
                    new ResourceNotFoundException("Loan not Found"));
            payment.setLoanRequest(loanRequest);
        }
        paymentRepository.save(payment);

        return payment;
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
        paymentDto.allocated = payment.getAllocated();

        if(payment.getLoan() != null) {
            paymentDto.loanId = payment.getLoan().getLoanId();
        }
        if(payment.getLoanRequest() != null) {
            paymentDto.loanRequestId = payment.getLoanRequest().getId();
        }

        return paymentDto;
    }

}
