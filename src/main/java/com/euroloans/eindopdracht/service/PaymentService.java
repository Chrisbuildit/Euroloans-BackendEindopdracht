package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.PaymentDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.*;
import com.euroloans.eindopdracht.security.UserIdentification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final LoanRequestRepository loanRequestRepository;

    private final InvestmentRepository investmentRepository;

    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository, LoanRequestRepository loanRequestRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.investmentRepository = investmentRepository;
    }

    public Payment createPayment(PaymentDto paymentDto) {

        Payment payment = new Payment();
        payment.setDate(java.time.LocalDate.now());
        payment.setAmount(paymentDto.amount);
        payment.setPaymentReference(paymentDto.paymentReference);

        User user = userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName()).
                orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

        payment.setUser(user);

        Role role = user.getRole();
        if (role.getRolename().equals("ROLE_BORROWER")) {
            payment.setLoanId(paymentDto.loanId);
        }

        LoanRequest loanRequest = loanRequestRepository.findById(paymentDto.loanRequestId).orElseThrow(() ->
                new ResourceNotFoundException("LoanRequest not Found"));
        payment.setLoanRequest(loanRequest);
        paymentRepository.save(payment);

        return payment;
    }

    //Allocates no allocated payments to
    public List<Payment> allocateAllPayments() {

        List<Payment> allocatedPayments = new ArrayList<>();

        Iterable<Investment> investments = investmentRepository.findAll();
        Iterable<Payment> payments = paymentRepository.findAll();
        for (Investment investment : investments) {
            for (Payment payment : payments) {
                //Matches payment with investment based on loanRequestId
                if (Objects.equals(investment.getLoanRequestId(), payment.getLoanRequest().getId()) &&
                        payment.getAllocated().equals(false)) {
                    if(investment.getLoans()!=null && Objects.equals(investment.getBalance(), investment.getLoans().getBalance())) {
                        payment.setLoanId(investment.getLoans().getLoanId());
                        payment.setInvestmentId(investment.getInvestmentId());
                        payment.setAllocated(true);
                        allocatedPayments.add(payment);
                        if (payment.getUser().getRole().getRolename().equals("ROLE_LENDER")) {
                            investment.increaseBalance(payment.getAmount());
                            investment.getLoans().increaseBalance(payment.getAmount());
                        } else if (payment.getUser().getRole().getRolename().equals("ROLE_BORROWER")) {
                            investment.decreaseBalance(payment.getAmount());
                            investment.getLoans().decreaseBalance(payment.getAmount());
                        }
                        investmentRepository.save(investment);
                        paymentRepository.save(payment);
                    }
                }
            }
        }
        return allocatedPayments;
    }

    public PaymentDto getPayment(Long paymentId) {
        Payment p = paymentRepository.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Loan not Found"));

        return transferToDto(p);
    }

    public List<PaymentDto> getUnallocatedPayments() {
        Iterable<Payment> pList = paymentRepository.findAll();
        List<PaymentDto> pDtoList = new ArrayList<>();

        for(Payment payment : pList) {
            if (payment.getAllocated().equals(false)) {
                PaymentDto paymentDto = transferToDto(payment);
                pDtoList.add(paymentDto);
            }
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

        if(payment.getLoanId() != null) {
            paymentDto.loanId = payment.getLoanId();
        }
        if(payment.getLoanRequest() != null) {
            paymentDto.loanRequestId = payment.getLoanRequest().getId();
        }
        if(payment.getInvestmentId() != null) {
            paymentDto.investmentId = payment.getInvestmentId();
        }

        return paymentDto;
    }

}
