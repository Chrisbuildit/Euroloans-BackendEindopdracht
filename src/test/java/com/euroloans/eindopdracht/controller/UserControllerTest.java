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

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username="testuser", roles="USER")       // check authorization, not authentication
    void getUser() throws Exception {

        UserDto udto = new UserDto();
        udto.username = "EMP";


        Mockito.when(userService.getUser("EMP")).thenReturn(udto);
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void createUser() {
    }
}