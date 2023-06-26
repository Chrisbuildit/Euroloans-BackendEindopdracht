package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanApplicationDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanApplicationRepository;
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
public class LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepos;

    private final UserRepository userRepos;

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepos, UserRepository userRepos) {
        this.loanApplicationRepos = loanApplicationRepos;
        this.userRepos = userRepos;
    }

    public Long createLoanApplication(LoanApplicationDto loanApplicationDto) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setName(loanApplicationDto.name);

        List<User> loanApplicationUsers = new ArrayList<>();
        for (String username : loanApplicationDto.usernameId) {
            User or = userRepos.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not Found"));

            loanApplicationUsers.add(or);
        }
        loanApplication.setUsers(loanApplicationUsers);


        loanApplicationRepos.save(loanApplication);

        return loanApplication.getId();
    }

    public LoanApplicationDto getLoanApplication(Long id) {
        LoanApplication l = loanApplicationRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("loanApplication not Found"));

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

    public List<LoanApplicationDto> getAllLoanApplications() {
        Iterable<LoanApplication> lList = loanApplicationRepos.findAll();
        List<LoanApplicationDto> lDtoList = new ArrayList<>();

        for(LoanApplication l : lList) {
            LoanApplicationDto loanApplicationDto = transferToDto(l);
            lDtoList.add(loanApplicationDto);
        }
        return lDtoList;
    }

    public LoanApplicationDto approveLoan(Long id, LoanApplicationDto loanApplicationDto) {
        Optional<LoanApplication> courseOptional = loanApplicationRepos.findById(id);
        if (courseOptional.isPresent()) {
            LoanApplication loanApplication = courseOptional.get();

            loanApplication.setApproved(loanApplicationDto.isApproved);
            LoanApplication returnLoanApplication = loanApplicationRepos.save(loanApplication);

            LoanApplicationDto dto = new LoanApplicationDto();
            dto.isApproved = returnLoanApplication.getApproved();

            return dto;
        }
        else {
            throw new ResourceNotFoundException("no LoanApplication found");
        }
    }

    public LoanApplicationDto transferToDto(LoanApplication loanApplication) {
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        loanApplicationDto.id = loanApplication.getId();
        loanApplicationDto.name = loanApplication.getName();
        loanApplicationDto.isApproved = loanApplication.getApproved();

        List<String> usernames = new ArrayList<>();
        for (User u : loanApplication.getUsers()) {
            usernames.add(u.getUsername());
        }
        loanApplicationDto.usernameId = usernames;
        return loanApplicationDto;
    }
}
