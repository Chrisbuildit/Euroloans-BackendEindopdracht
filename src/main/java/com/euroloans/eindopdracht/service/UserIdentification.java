package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Getter
@Service
//Returns username contained in User-token
public class UserIdentification {

    private final UserRepository userRepos;

    public UserIdentification(UserRepository userRepos) {
        this.userRepos = userRepos;
    }

    public User getCurrentUser() {

        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentuser;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            currentUserName = authentication.getName();

            currentuser = userRepos.findById(currentUserName).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

        } else {
            throw new ResourceNotFoundException("User no longer exist");
        }
        return currentuser;
    }

}


