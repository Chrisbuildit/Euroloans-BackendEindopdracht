package com.euroloans.eindopdracht.security;

import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Getter
//Returns username contained in User-token
public class UserIdentification {

    private final UserRepository userRepos;

    public UserIdentification(UserRepository userRepos) {
        this.userRepos = userRepos;
    }

    public User getCurrentUser() {

        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser;
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication!=null) {

            currentUserName = authentication.getName();

            currentUser = userRepos.findById(currentUserName).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        } else if(authentication==null) {
            Role role = new Role();
            role.setRolename("ROLE_BORROWER");

            currentUser = new User();
            currentUser.setUsername("test");
            currentUser.setRole(role);
        } else {
            throw new ResourceNotFoundException("User no longer exist");
        }
        return currentUser;
    }

}


