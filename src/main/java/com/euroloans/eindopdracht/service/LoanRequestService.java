package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanRequestService {
    private final LoanRequestRepository loanRequestRepos;
    private final UserRepository userRepos;

    public LoanRequestService(LoanRequestRepository loanRequestRepos, UserRepository userRepos) {
        this.loanRequestRepos = loanRequestRepos;
        this.userRepos = userRepos;
    }

    public LoanRequest createLoanRequest(LoanRequestDto loanRequestDto) {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setName(loanRequestDto.name);
        loanRequest.setAmount(loanRequestDto.amount);
        loanRequest.setApproved((loanRequestDto.isApproved));
        loanRequest.setUsernameId("Not applicable");

        User user = userRepos.findById(loanRequestDto.usernameId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found"));
        Role role = user.getRole();
        if (role.getRolename().equals("ROLE_BORROWER")) {
            if(loanRequest.users==null) {
                Map<String, User> map = new HashMap<>();
                loanRequest.setUsers(map);
            }
            loanRequest.addUsers("Borrower", user);
        } else {
            throw new ResourceNotFoundException("You do not have access");
        }

        //Iteration for list
//        List<User> loanRequestUsers = new ArrayList<>();
//        for (String username : loanRequestDto.usernameIds) {
//            User user = userRepos.findById(username).orElseThrow(() ->
//                    new ResourceNotFoundException("User not Found"));
//
//            loanRequestUsers.add(user);
//        }
//        loanRequest.setUsers(loanRequestUsers);

        loanRequestRepos.save(loanRequest);

        return loanRequest;
    }

    public LoanRequestDto getLoanRequest(Long id) {
        LoanRequest l = loanRequestRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("loanRequest not Found"));

        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();

            User currentuser = userRepos.findById(currentUserName).orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

            if (currentuser.getRole().getRolename().equals("ROLE_BORROWER")) {

                if (l.getUsers().containsValue(currentuser)) {
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

    public LoanRequest updateLoan(Long id, LoanRequestDto loanRequestDto) {
        Optional<LoanRequest> courseOptional = loanRequestRepos.findById(id);
        if (courseOptional.isPresent()) {
            LoanRequest loanRequest = courseOptional.get();
            LoanRequest updatedLoanRequest = new LoanRequest();

            UserIdentification userIdentification = new UserIdentification(userRepos);
            User user = userIdentification.getCurrentUser();

            if (user.getRole().getRolename().equals("ROLE_EMPLOYEE")) {

                updatedLoanRequest.setApproved(loanRequestDto.isApproved);
                updatedLoanRequest.setAmount(loanRequest.getAmount());

            } else {
                if (user.getRole().getRolename().equals("ROLE_BORROWER")
                        && loanRequest.getUsers().containsValue(user)) {

                    updatedLoanRequest.setAmount(loanRequestDto.amount);
                    updatedLoanRequest.setApproved(loanRequest.getApproved());

                } else {
                    throw new ResourceNotFoundException("You do not have access");
                }
            }

            updatedLoanRequest.setUsernameId("Not applicable");
            updatedLoanRequest.setName(loanRequest.getName());
            updatedLoanRequest.setUsers(loanRequest.getUsers());
            updatedLoanRequest.setId(loanRequest.getId());

            loanRequestRepos.save(updatedLoanRequest);

            return updatedLoanRequest;
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
        loanRequestDto.amount = loanRequest.getAmount();
        loanRequestDto.usernameIds = loanRequest.getUsers();

        //Iteration for list
//        List<String> usernames = new ArrayList<>();
//        for (User u : loanRequest.getUsers()) {
//            usernames.add(u.getUsername());
//        }
//        loanRequestDto.usernameIds = usernames;

        return loanRequestDto;
    }
}
