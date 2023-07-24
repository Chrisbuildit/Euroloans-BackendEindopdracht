package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    UserRepository userRepos;

    @Mock
    RoleRepository roleRepos;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    UserService service;

    public PasswordEncoder passwordEncoder(String password) {
        return new BCryptPasswordEncoder();
    }

    User user1;
    UserDto userDto1;

    Role role1;

    User user2;
    UserDto userDto2;

    Role role2;

    @BeforeEach
    void setup() {

        role1 = new Role();
        role1.setRolename("ROLE_LENDER");

        role2 = new Role();
        role2.setRolename("ROLE_BORROWER");

        user1 = new User();
        user1.setUsername("Roel");
        user1.setPassword("Confidential");
        user1.setRole(role1);

        user2 = new User();
        user2.setUsername("Ruard");
        user2.setPassword("Confidential");
        user2.setRole(role2);

        userDto1 = new UserDto();
        userDto1.username = "Roel";
        userDto1.password = "Confidential";
        userDto1.rolenameId = "ROLE_LENDER";

        userDto2 = new UserDto();
        userDto2.username = "Ruard";
        userDto2.password = "Confidential";
        userDto2.rolenameId = "ROLE_BORROWER";
    }

    @Test
    void createUser() {
        when(roleRepos.findById(anyString())).thenReturn(Optional.of(role1));
//        user1.setPassword(passwordEncoder(anyString()).encode("Confidential"));

        User result = service.createUser(userDto1);

        assertEquals("Roel", result.getUsername());
        assertEquals("ROLE_LENDER", result.getRole().getRolename());
    }

    @Test
    void getUser() {

        when(userRepos.findById(anyString())).thenReturn(Optional.of(user1));

        UserDto userDto = service.getUser("Roel");

        assertEquals("Roel", userDto.username);
        assertEquals("Confidential", userDto.password);
        assertEquals("ROLE_LENDER", userDto.rolenameId);
    }

    @Test
    void getAllUsers() {
        List<User> uList = new ArrayList<>();
        uList.add(user1);
        uList.add(user2);

        when(userRepos.findAll()).thenReturn(uList);

        List<UserDto> result = service.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Roel", result.get(0).username);
        assertEquals("ROLE_BORROWER", result.get(1).rolenameId);
    }


}