package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.security.JwtService;
import com.euroloans.eindopdracht.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username="BOR", roles="BORROWER")       // check authorization, not authentication
    void shouldRetrieveCorrectUser() throws Exception {

        UserDto userDto = new UserDto();
        userDto.username = "Batavus fiets";
        userDto.password = "test";
        userDto.rolenameId = "ROLE_BORROWER";
        userDto.loans = new ArrayList<>(1);
        userDto.loanRequests = new ArrayList<>(1);

        Mockito.when(userService.getUser("BOR")).thenReturn(userDto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productname", is("Batavus fiets")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unitprice", is(1500.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity", is(5)));
    }


    @Test
    void getAllUsers() {
    }

    @Test
    void createUser() {
    }
}