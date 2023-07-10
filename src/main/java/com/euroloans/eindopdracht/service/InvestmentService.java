package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.InvestmentDto;
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
import java.util.Optional;

@Service
public class InvestmentService {

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final LoanRepository loanRepository;

    private final InvestmentRepository investmentRepository;

    public InvestmentService(UserRepository userRepository, PaymentRepository paymentRepository, LoanRepository loanRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
        this.investmentRepository = investmentRepository;
    }

    public Investment createInvestment(InvestmentDto investmentDto) {
        Investment investment = new Investment();

        User lender = userRepository.findById(investmentDto.usernameId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found"));
        investment.addUsers(lender);

        UserIdentification userIdentification = new UserIdentification(userRepository);
        User employee = userIdentification.getCurrentUser();
        investment.addUsers(employee);

        List<Payment> investmentPayment = new ArrayList<>();
        for (Long p : investmentDto.paymentList) {
            Optional<Payment> optionalPayment = paymentRepository.findById(p);
            optionalPayment.ifPresent(investmentPayment::add);
            optionalPayment.ifPresent(payment -> investment.increaseBalance(payment.getAmount()));
            optionalPayment.ifPresent(payment -> investment.setLoanRequestId(payment.getLoanRequest().getId()));
            optionalPayment.ifPresent(payment -> payment.setAllocated(true));
        }
        investment.setPayments(investmentPayment);
        investmentRepository.save(investment);

        return investment;
    }

    public Investment adjustBalance(Long id) {
        Optional<Investment> investmentOptional = investmentRepository.findById(id);
        if (investmentOptional.isPresent()) {
            Investment investment = investmentOptional.get();

            //Copying of values
            Investment updatedInvestment;
            updatedInvestment = investment;

            UserIdentification userIdentification = new UserIdentification(userRepository);
            User user = userIdentification.getCurrentUser();

            //Previous payments
//            List<Long> prevPaymentIds = new ArrayList<>();
//            Iterable<Payment> prevPayments = new ArrayList<>(investment.getPayments());
//            for (Payment p : prevPayments)
//                prevPaymentIds.add(p.getPaymentId());

            Iterable<Payment> payments = paymentRepository.findAll();
            for (Payment payment : payments) {

                if (payment.getAllocated().equals(false)) {
                    if (user.getRole().getRolename().equals("ROLE_LENDER")) {
                        updatedInvestment.increaseBalance(payment.getAmount());
                    } else if (user.getRole().getRolename().equals("ROLE_BORROWER")) {
                        updatedInvestment.decreaseBalance(payment.getAmount());
                    }
                    updatedInvestment.addPayment(payment);
                    updatedInvestment.setLoanRequestId(investment.getLoanRequestId());
                    payment.setAllocated(true);
                }
            }

            investmentRepository.save(updatedInvestment);
            return updatedInvestment;
    } else {
        throw new ResourceNotFoundException("You do not have access");
        }
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
        investmentDto.users = investment.getUsers();
        investmentDto.balance = investment.getBalance();
        investmentDto.loanRequestId = investment.getLoanRequestId();
        investmentDto.payments = investment.getPayments();

        if(investment.getLoan() != null) {
            investmentDto.loanId = investment.getLoan().getLoanId();
        }

        return investmentDto;
    }
    }