package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepos;

    private final RoleRepository roleRepos;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepos, RoleRepository roleRepos, PasswordEncoder encoder) {
        this.userRepos = userRepos;
        this.roleRepos = roleRepos;
        this.encoder = encoder;
    }

    public String createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.username);
        newUser.setPassword(encoder.encode(userDto.password));

        Role tempRole = roleRepos.findById("ROLE_" + userDto.rolenameId).orElseThrow(() -> new ResourceNotFoundException("Role not Found"));
        newUser.setRole(tempRole);

        userRepos.save(newUser);

        return "Done";
    }

    public UserDto getUser(String username) {
        User t = userRepos.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        return transferToDto(t);
    }

    public List<UserDto> getAllUsers() {
        Iterable<User> uList = userRepos.findAll();
        List<UserDto> uDtoList = new ArrayList<>();

        for(User u : uList) {
            UserDto userDto = transferToDto(u);
            uDtoList.add(userDto);
        }
        return uDtoList;
    }

    public UserDto transferToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.username = user.getUsername();
        userDto.password = "Confidential";
        userDto.rolenameId = user.getRole().getRolename();

        List<String> loanApplications = new ArrayList<>();
        for (LoanApplication l : user.getLoanApplications()) {
            loanApplications.add(l.getName());
        }
        userDto.loanApplications = loanApplications;

        return userDto;
    }
}
