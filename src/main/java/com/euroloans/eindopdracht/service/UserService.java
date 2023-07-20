package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.*;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepos;

    private final RoleRepository roleRepos;

    public PasswordEncoder passwordEncoder(String password) {
        return new BCryptPasswordEncoder();
    }

    public UserService(UserRepository userRepos, RoleRepository roleRepos) {
        this.userRepos = userRepos;
        this.roleRepos = roleRepos;
    }


    public User createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.username);

        newUser.setPassword(passwordEncoder(userDto.password).encode(userDto.password));

        Role tempRole = roleRepos.findById("ROLE_" + userDto.rolenameId).orElseThrow(() -> new ResourceNotFoundException("Role not Found"));
        newUser.setRole(tempRole);

        userRepos.save(newUser);

        return newUser;
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

        List<String> loanRequests = new ArrayList<>();
        if(user.getLoanRequests()!=null) {
            for (LoanRequest l : user.getLoanRequests()) {
                loanRequests.add(l.getName());
            }
            userDto.loanRequests = loanRequests;
        }

        List<Long> loans = new ArrayList<>();
        if(user.getLoans()!=null) {
            for (Loan loan : user.getLoans()) {
                loans.add(loan.getLoanId());
            }
            userDto.loans = loans;
        }

        List<Long> investments = new ArrayList<>();
        if(user.getInvestments()!=null) {
            for (Investment investment : user.getInvestments()) {
                investments.add(investment.getInvestmentId());
            }
            userDto.investments = investments;
        }
        return userDto;
    }
}
