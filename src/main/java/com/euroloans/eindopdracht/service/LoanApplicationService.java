package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanApplicationDto;
import com.euroloans.eindopdracht.model.LoanApplication;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.LoanApplicationRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        User user = userRepos.findById(loanApplicationDto.usernameId).get(); //happy flow
        loanApplication.setUser(user);
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
        loanApplicationDto.usernameId = loanApplication.getUser().getUsername();

        return loanApplicationDto;
    }
}
