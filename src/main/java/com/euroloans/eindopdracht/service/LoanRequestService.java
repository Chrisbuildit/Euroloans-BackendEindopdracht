package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LoanRequestService {
    private final LoanRequestRepository loanRequestRepos;

    private final UserRepository userRepos;

    public LoanRequestService(LoanRequestRepository loanRequestRepos, UserRepository userRepos) {
        this.loanRequestRepos = loanRequestRepos;
        this.userRepos = userRepos;
    }

    public Long createLoanRequest(LoanRequestDto loanRequestDto) {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setName(loanRequestDto.name);

        List<User> loanRequestUsers = new ArrayList<>();
        for (String username : loanRequestDto.usernameIds) {
            User user = userRepos.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

            loanRequestUsers.add(user);
        }
        loanRequest.setUsers(loanRequestUsers);


        loanRequestRepos.save(loanRequest);

        return loanRequest.getId();
    }

    public LoanRequestDto getLoanRequest(Long id) {
        LoanRequest l = loanRequestRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("loanRequest not Found"));

        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();

            User currentuser = userRepos.findById(currentUserName).orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

            if (currentuser.getRole().getRolename().equals("ROLE_BORROWER")) {

                if (l.getUsers().contains(currentuser)) {//
                    return transferToDto(l);
                } else {
                    throw new ResourceNotFoundException("You do not have access");
                }

            } else {
                return transferToDto(l);
            }
        } else {
            throw new ResourceNotFoundException("Authentication error");
        }
    }

    public List<LoanRequestDto> getAllLoanRequests() {
        Iterable<LoanRequest> lList = loanRequestRepos.findAll();
        List<LoanRequestDto> lDtoList = new ArrayList<>();

        for(LoanRequest l : lList) {
            LoanRequestDto loanRequestDto = transferToDto(l);
            lDtoList.add(loanRequestDto);
        }
        return lDtoList;
    }

    public String approveLoan(Long id, LoanRequestDto loanRequestDto) {
        Optional<LoanRequest> courseOptional = loanRequestRepos.findById(id);
        if (courseOptional.isPresent()) {
            LoanRequest loanRequest = courseOptional.get();

            loanRequest.setApproved(loanRequestDto.isApproved);

            User user = userRepos.findById(loanRequestDto.usernameId).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
            loanRequest.users.add(user);

            loanRequestRepos.save(loanRequest);

            return "LoanRequest approved";
        }
        else {
            throw new ResourceNotFoundException("no LoanRequest found");
        }
    }

    public LoanRequestDto transferToDto(LoanRequest loanRequest) {
        LoanRequestDto loanRequestDto = new LoanRequestDto();
        loanRequestDto.id = loanRequest.getId();
        loanRequestDto.name = loanRequest.getName();
        loanRequestDto.isApproved = loanRequest.getApproved();
        loanRequestDto.usernameId = "Not applicable";

        List<String> usernames = new ArrayList<>();
        for (User u : loanRequest.getUsers()) {
            usernames.add(u.getUsername());
        }
        loanRequestDto.usernameIds = usernames;
        return loanRequestDto;
    }
}
