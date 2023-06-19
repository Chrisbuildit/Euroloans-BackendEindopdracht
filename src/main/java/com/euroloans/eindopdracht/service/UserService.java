//package com.euroloans.eindopdracht.service;
//
//import com.euroloans.eindopdracht.dto.UserDto;
//import com.euroloans.eindopdracht.exception.ResourceNotFoundException;
//import com.euroloans.eindopdracht.model.Role;
//import com.euroloans.eindopdracht.model.User;
//import com.euroloans.eindopdracht.repository.RoleRepository;
//import com.euroloans.eindopdracht.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepos;
//
//    private final RoleRepository roleRepos;
//
//    private final PasswordEncoder encoder;
//
//    public UserService(UserRepository userRepos, RoleRepository roleRepos, PasswordEncoder encoder) {
//        this.userRepos = userRepos;
//        this.roleRepos = roleRepos;
//        this.encoder = encoder;
//    }
//
//    public String createUser(UserDto userDto) {
//        User newUser = new User();
//        newUser.setUsername(userDto.username);
//        newUser.setPassword(encoder.encode(userDto.password));
//
//        List<Role> userRoles = new ArrayList<>();
//        for (String rolename : userDto.roles) {
//            Optional<Role> or = roleRepos.findById("ROLE_" + rolename);
//
//            userRoles.add(or.get());
//        }
//        newUser.setRoles(userRoles);
//
//        userRepos.save(newUser);
//
//        return "Done";
//    }
//
//    public UserDto getUser(String username) {
//        User t = userRepos.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
//
//        UserDto userDto = new UserDto();
//        userDto.username = t.getUsername();
//        //Figure uit:
////        userDto.roles = t.getRoles();
//
//        return userDto;
//    }
//}
