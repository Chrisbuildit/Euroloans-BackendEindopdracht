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
import java.util.Objects;
import java.util.Optional;

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
            payment.setLoanId(paymentDto.loanId);
        }

        LoanRequest loanRequest = loanRequestRepository.findById(paymentDto.loanRequestId).orElseThrow(() ->
                new ResourceNotFoundException("LoanRequest not Found"));
        payment.setLoanRequest(loanRequest);
        paymentRepository.save(payment);

        return payment;
    }

    //Allocates no allocated payments to
    public List<Investment> allocateAllPayments() {
//        Optional<Investment> investmentOptional = investmentRepository.findById(id);
//        if (investmentOptional.isPresent()) {
//            Investment investment = investmentOptional.get();

        //Copying of values
//            Investment updatedInvestment;
//            updatedInvestment = investment;

        //Previous payments
//            List<Long> prevPaymentIds = new ArrayList<>();
//            Iterable<Payment> prevPayments = new ArrayList<>(investment.getPayments());
//            for (Payment p : prevPayments)
//                prevPaymentIds.add(p.getPaymentId());

        List<Investment> updatedInvestments = new ArrayList<>();

        Iterable<Investment> investments = investmentRepository.findAll();
        Iterable<Payment> payments = paymentRepository.findAll();
        for (Investment investment : investments) {
            for (Payment payment : payments) {
                //Matches payment with investment based on loanRequestId
                if (Objects.equals(investment.getLoanRequestId(), payment.getLoanRequest().getId()) &&
                        payment.getAllocated().equals(false)) {
                    if (payment.getUser().getRole().getRolename().equals("ROLE_LENDER")) {
                        investment.increaseBalance(payment.getAmount());
                        //Allocates payment to loan if loan exist and balance correspond with investment balance
                        if(investment.getLoan()!=null && Objects.equals(investment.getBalance(), investment.getLoan().getLoanRequest().getAmount())) {
                            investment.getLoan().increaseBalance(payment.getAmount());
                        }
                    } else if (payment.getUser().getRole().getRolename().equals("ROLE_BORROWER")) {
                        investment.decreaseBalance(payment.getAmount());
                        if(investment.getLoan()!=null) {
                            investment.getLoan().decreaseBalance(payment.getAmount());
                            payment.setLoanId(investment.getLoan().getLoanId());
                    }}
                    payment.setInvestmentId(investment.getInvestmentId());
                    payment.setAllocated(true);
                    updatedInvestments.add(investment);
                    investmentRepository.save(investment);
                    paymentRepository.save(payment);
                }
            }
        }
        return updatedInvestments;
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
