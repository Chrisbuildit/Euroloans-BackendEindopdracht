package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanApplicationDto;
import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanApplicationRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
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

    public List<LoanApplicationDto> getAllLoanApplications() {
        Iterable<LoanApplication> lList = loanApplicationRepos.findAll();
        List<LoanApplicationDto> lDtoList = new ArrayList<>();

        for(LoanApplication l : lList) {
            LoanApplicationDto loanApplicationDto = transferToDto(l);
            lDtoList.add(loanApplicationDto);
        }
        return lDtoList;
    }

    public LoanApplicationDto transferToDto(LoanApplication loanApplication) {
        LoanApplicationDto loanApplicationDto = new LoanApplicationDto();
        loanApplicationDto.id = loanApplication.getId();
        loanApplicationDto.name = loanApplication.getName();
//        loanApplicationDto.usernameId = loanApplication.getUsers();

//        for (User u : loanApplication.getUsers()) {
//            String[] usernames = new String[u.getUsername().length()];
//            loanApplicationDto.usernameId = Arrays.fill(usernames,u.getUsername());
//            }
//        }

        return loanApplicationDto;
    }
}
