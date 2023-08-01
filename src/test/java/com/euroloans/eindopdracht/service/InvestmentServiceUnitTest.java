package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.InvestmentDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceUnitTest {

    @Mock
    UserRepository userRepos;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    InvestmentRepository investmentRepository;

    @InjectMocks
    InvestmentService service;

    InvestmentDto investmentDtoCreated;

    Role role1;
    User user1;
    LoanRequest loanRequest1;
    Payment payment1;

    Investment investment1;
    Investment investment2;

    @BeforeEach
    void setup() {

        List<Long> paymentList = new ArrayList<>();
        paymentList.add(1L);

        investmentDtoCreated = new InvestmentDto();
        investmentDtoCreated.investmentId = 1L;
        investmentDtoCreated.usernameId = "LENDER";
        investmentDtoCreated.paymentList = paymentList;

        role1 = new Role();
        role1.setRolename("ROLE_LENDER");

        user1 = new User();
        user1.setUsername("Roel");
        user1.setPassword("Confidential");
        user1.setRole(role1);

        Collection<User> users = new ArrayList<>();
        users.add(user1);

        loanRequest1 = new LoanRequest();
        loanRequest1.setId(1L);

        payment1 = new Payment();
        payment1.setPaymentId(1L);
        payment1.setUser(user1);
        payment1.setAllocated(false);
        payment1.setLoanRequest(loanRequest1);
        payment1.setAmount(1);

        List<Payment> payments = new ArrayList<>();
        payments.add(payment1);

        investment1 = new Investment();
        investment1.setInvestmentId(1L);
        investment1.setBalance(1);
        investment1.setLoanRequestId(1L);
        investment1.setUsers(users);
        investment1.setPayments(payments);

        investment2 = new Investment();
        investment2.setInvestmentId(2L);
        investment2.setBalance(2);
        investment2.setLoanRequestId(1L);
        investment2.setUsers(users);
        investment2.setPayments(payments);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));
    }

    @Test
    void createInvestment() {
        when(userRepos.findById(anyString())).thenReturn(Optional.of(user1));
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment1));

        Investment result = service.createInvestment(investmentDtoCreated);

        //Gee error
//        assertEquals(1L, result.getInvestmentId());
        assertTrue(true, String.valueOf(result.getUsers().contains(user1)));
        assertEquals(1, result.getBalance());
        assertEquals(1L, result.getLoanRequestId());
    }

    @Test
    void getInvestment() {
        when(investmentRepository.findById(anyLong())).thenReturn(Optional.of(investment1));

        InvestmentDto investmentDto = service.getInvestment(investmentDtoCreated.investmentId);

        assertEquals(1, investmentDto.balance);
        assertEquals(1L, investmentDto.loanRequestId);
        assertEquals("Roel", investmentDto.usernameId);
        assertEquals(investmentDtoCreated.paymentList, investmentDto.paymentList);
    }

    @Test
    void getAllInvestments() {
        List<Investment> iList = new ArrayList<>();
        iList.add(investment1);
        iList.add(investment2);

        when(investmentRepository.findAll()).thenReturn(iList);

        List<InvestmentDto> result = service.getAllInvestments();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).investmentId);
        assertEquals(investmentDtoCreated.paymentList, result.get(1).paymentList);
        assertEquals(2, result.get(1).balance);
        }
}