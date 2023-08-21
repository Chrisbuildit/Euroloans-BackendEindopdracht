package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepos;
    @Autowired
    RoleRepository roleRepository;

    UserDto userDto;

    User user;
    Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setRolename("ROLE_BORROWER");

        roleRepository.save(role);

        user = new User();
        user.setUsername("BOR");
        user.setPassword("Confidential");
        user.setRole(role);

        userRepos.save(user);

        userDto = new UserDto();
        userDto.username = "Test";
        userDto.password = "test";
        userDto.rolenameId = "BORROWER";
    }

    @Test
    void shouldCreateUserTest() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.post("/users", userDto)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userDto)))
                    .andExpect(status().isCreated());
        }

    @Test
    void getUserTest() throws Exception {

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/users/{id}", user.getUsername()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("BOR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", is("Confidential")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rolenameId", is("ROLE_BORROWER")));
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
}