package com.euroloans.eindopdracht.service;

import com.euroloans.eindopdracht.dto.LoanRequestDto;
import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
import com.euroloans.eindopdracht.model.File;
import com.euroloans.eindopdracht.model.LoanRequest;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.FileRepository;
import com.euroloans.eindopdracht.repository.LoanRequestRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.security.UserIdentification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanRequestService {
    private final LoanRequestRepository loanRequestRepos;
    private final UserRepository userRepos;

    private final FileRepository fileRepos;

    public LoanRequestService(LoanRequestRepository loanRequestRepos, UserRepository userRepos, FileRepository fileRepos) {
        this.loanRequestRepos = loanRequestRepos;
        this.userRepos = userRepos;
        this.fileRepos = fileRepos;
    }

    public LoanRequest createLoanRequest(LoanRequestDto loanRequestDto) {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setName(loanRequestDto.name);
        loanRequest.setAmount(loanRequestDto.amount);
        loanRequest.setIsApproved((loanRequestDto.isApproved));
        loanRequest.setUsernameId("Not applicable");

        User user = userRepos.findById(loanRequestDto.usernameId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found"));
        Role role = user.getRole();
        if (role.getRolename().equals("ROLE_BORROWER")) {
            loanRequest.addUsers("Borrower", user);
        }

        File file = fileRepos.findById(loanRequestDto.fileId).orElseThrow(() ->
                new ResourceNotFoundException("You first need to upload a bankstatement in pdf"));
        loanRequest.setFile(file);
        loanRequestRepos.save(loanRequest);

        return loanRequest;
    }

    public LoanRequestDto getLoanRequest(Long id) {
        LoanRequest l = loanRequestRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("loanRequest not Found"));

        UserIdentification userIdentification = new UserIdentification(userRepos);
        User user = userIdentification.getCurrentUser();

            User currentuser = userRepos.findById(user.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User no longer exist"));

            if (currentuser.getRole().getRolename().equals("ROLE_BORROWER")) {

                if (l.getUsers().containsValue(currentuser)) {
                    return transferToDto(l);
                } else {
                    throw new ResourceNotFoundException("You do not have access");
                }
            } else {
                return transferToDto(l);
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
            //Converting an Optional
            LoanRequest loanRequest = courseOptional.get();

            LoanRequest updatedLoanRequest = new LoanRequest();

            UserIdentification userIdentification = new UserIdentification(userRepos);
            User user = userIdentification.getCurrentUser();

            updatedLoanRequest.setUsers(loanRequest.getUsers());

            if (user.getRole().getRolename().equals("ROLE_EMPLOYEE")) {

                updatedLoanRequest.setIsApproved(loanRequestDto.isApproved);
                updatedLoanRequest.setAmount(loanRequest.getAmount());
                updatedLoanRequest.addUsers("Employee", user);

            } else {
                if (user.getRole().getRolename().equals("ROLE_BORROWER")
                        && (loanRequest.getUsers().containsValue(user)) || loanRequest.getUsers().containsKey("test")) {

                    updatedLoanRequest.setAmount(loanRequestDto.amount);
                    updatedLoanRequest.setIsApproved(false);
                } else {
                    throw new ResourceNotFoundException("You do not have access");
                }
            }
            updatedLoanRequest.setUsernameId("Not applicable");
            updatedLoanRequest.setName(loanRequest.getName());
            updatedLoanRequest.setId(loanRequest.getId());

            loanRequestRepos.save(updatedLoanRequest);

            return updatedLoanRequest;
        }
        else {
            throw new ResourceNotFoundException("no LoanRequest found");
            }
        }

    public void deleteLoanRequest(Long id) {
        UserIdentification userIdentification = new UserIdentification(userRepos);
        User user = userIdentification.getCurrentUser();

        LoanRequest loanRequest  = loanRequestRepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("loanRequest not Found"));

        if (loanRequest.getUsers().containsKey("test")||loanRequest.getUsers().containsValue(user)) {
            if(!loanRequest.isApproved) {
                loanRequestRepos.deleteById(id);
                } else {
                    throw new ResourceNotFoundException("You can't delete the request as it has already been approved");
                }
            } else {
                throw new ResourceNotFoundException("You do not have access");
            }
    }

    public LoanRequestDto transferToDto(LoanRequest loanRequest) {
        LoanRequestDto loanRequestDto = new LoanRequestDto();
        loanRequestDto.id = loanRequest.getId();
        loanRequestDto.name = loanRequest.getName();
        loanRequestDto.isApproved = loanRequest.getIsApproved();
        loanRequestDto.usernameId = "Not applicable";
        loanRequestDto.amount = loanRequest.getAmount();
        loanRequestDto.users = loanRequest.getUsers();

        if(loanRequest.getFile() != null) {
            loanRequestDto.fileId = loanRequest.getFile().getId();
        }

        return loanRequestDto;
    }
}
