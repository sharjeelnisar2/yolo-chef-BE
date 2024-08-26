package com.yolo.chef.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoResponse getUserInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");
        Map<String, Object> roles = jwt.getClaim("resource_access");

        return new UserInfoResponse(username, email, name, roles);
    }

    public CheckUserResponse checkUser(String username) {
        boolean isUserCreated = userRepository.existsByUsername(username);
        boolean isUserProfileCompleted = userRepository.isProfileCompleted(username); // Implement this in the repository

        return new CheckUserResponse(isUserCreated, isUserProfileCompleted);
    }
}
