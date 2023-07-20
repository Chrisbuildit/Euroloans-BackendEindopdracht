package com.euroloans.eindopdracht.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateCorrectUser() throws Exception {

        String requestJson = """
                {
                    "username": "BOR",
                    "password": "Confidential",
                    "rolenameId": "ROLE_BORROWER"                    
                }
                """;

//        "loans": ["1"],
//        "loanRequests": ["1"]

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/username")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

//    @Test
//    void shouldGetCorrectUser() throws Exception {
//        String requestJson = """
//                {
//                    "username": "BOR",
//                    "password": "Confidential",
//                    "rolenameId": "ROLE_BORROWER",
//                    "loans": ["1"],
//                    "loanRequests": ["1"]
//                }
//                """;
//
//        MvcResult postResult = this.mockMvc
//                .perform(MockMvcRequestBuilders.post("/username")
//                        .contentType(APPLICATION_JSON_UTF8)
//                        .content(requestJson))
//                .andReturn();
//
//        String responseJson = postResult.getResponse().getContentAsString();
////        String username = Integer.parseInt(responseJson);
//
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.get("/username", responseJson))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("BOR")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.password", is("Confidential")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.rolenameId", is("ROLE_BORROWER")));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.loans", is("1")));
////                .andExpect(MockMvcResultMatchers.jsonPath("$.loanRequests", is(["1"])));
//    }

    @Test
    void getAllUsers() {
    }


}