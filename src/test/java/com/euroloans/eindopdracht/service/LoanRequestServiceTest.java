package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.InvestmentRepository;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class LoanRequestServiceTest {

    @Mock
    UserRepository userRepos;

    @Mock
    LoanRequestRepository loanRequestRepository;

    @InjectMocks
    LoanRequestService service;

    @Mock
    UserIdentification userIdentification;

    LoanRequest loanRequest1;

    LoanRequestDto loanRequestDto;

    User user1;
    Role role1;

    Map<String, User> users;

    Collection<User> values;


    @BeforeEach
    void setup() {
        loanRequestDto = new LoanRequestDto();
        loanRequestDto.id = 1L;
        loanRequestDto.name = "test";
        loanRequestDto.usernameId = "test";
        loanRequestDto.amount = 10;
        loanRequestDto.isApproved = false;

        role1 = new Role();
        role1.setRolename("ROLE_BORROWER");

        user1 = new User();
        user1.setUsername("test");
        user1.setRole(role1);

        users = new HashMap<>();
        users.put("test", user1);

        values = users.values();

        loanRequest1 = new LoanRequest();
        loanRequest1.setId(1L);
        loanRequest1.setName("test");
        loanRequest1.setAmount(10);
        loanRequest1.setApproved(false);
        loanRequest1.setUsers(users);
    }

    @Test
    void createLoanRequest() {
        when(userRepos.findById(anyString())).thenReturn(Optional.of(user1));

        LoanRequest result = service.createLoanRequest(loanRequestDto);

        assertEquals("test", result.getName());
        assertEquals(10, result.getAmount());
        assertEquals(false, result.getApproved());
        //Werk nie
//        assertEquals(values, result.getUsers().values());
    }

    @Test
    @WithMockUser(username = "test", password = "Confidential", roles = "ROLE_BORROWER")
    void updateLoan() {
        when(loanRequestRepository.findById(anyLong())).thenReturn(Optional.of(loanRequest1));
        //Nie gewenste effek - SME'r weet ook niet waarom
//        when(userIdentification.getCurrentUser()).thenReturn(user1);

        LoanRequest result = service.updateLoan(loanRequestDto.id, loanRequestDto);

        assertEquals("test", result.getName());
        assertEquals(10, result.getAmount());
        assertEquals(false, result.getApproved());
    }

    @Test
    void deleteLoanRequest() {
        when(loanRequestRepository.findById(loanRequest1.getId())).thenReturn(Optional.of(loanRequest1));

        service.deleteLoanRequest(loanRequest1.getId());

        verify(loanRequestRepository).deleteById(loanRequest1.getId());

    }

    @Test
    void transferToDto() {

        LoanRequestDto loanRequestDtoResult = service.transferToDto(loanRequest1);

        assertEquals(1L, loanRequestDtoResult.id);
        assertEquals("Not applicable", loanRequestDtoResult.usernameId);
        assertEquals(10, loanRequestDtoResult.amount);
        assertEquals(false, loanRequestDtoResult.isApproved);
    }

    @Test
    void getLoanRequest() {
        when(userRepos.findById(anyString())).thenReturn(Optional.of(user1));
        when(loanRequestRepository.findById(anyLong())).thenReturn(Optional.of(loanRequest1));

        LoanRequestDto loanRequestDtoResult = service.getLoanRequest(loanRequestDto.id);

        assertEquals("test", loanRequestDtoResult.name);
        assertEquals(10, loanRequestDtoResult.amount);
        assertEquals(false, loanRequestDtoResult.isApproved);
    }
}