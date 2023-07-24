package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.service.LoanRequestService;
import com.euroloans.eindopdracht.service.UserIdentification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LoanRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepos;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    LoanRequestRepository loanRequestRepository;

    LoanRequestDto loanRequestDto;

    User user = new User();
    User deleteUser;
    Role role;

    LoanRequest loanRequest;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setRolename("ROLE_BORROWER");

        user.setUsername("BOR");
        user.setPassword("test");
        user.setRole(role);

        roleRepository.save(role);

        deleteUser = new User();
        deleteUser.setUsername("test");

        loanRequestDto = new LoanRequestDto();
        loanRequestDto.amount = 3;
        loanRequestDto.usernameId = "test";
        loanRequestDto.name = "test";
        loanRequestDto.isApproved = false;

        Map<String, User> users = new HashMap<>();
        users.put("Borrower", deleteUser);

        loanRequest = new LoanRequest();
        loanRequest.setId(1L);
        loanRequest.setUsers(users);
        loanRequestRepository.save(loanRequest);

        userRepos.save(user);
    }

    @Test
    void shouldCreateLoanRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/loanRequests", loanRequestDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequestDto)))
                .andExpect(status().isCreated());
    }

        public static String asJsonString(final Object obj) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    @Test
    void shouldDeleteLoanRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/loanRequests/" + loanRequest.getId())).andExpect(status().isNoContent());
    }
}