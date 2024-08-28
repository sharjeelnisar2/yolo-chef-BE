package com.yolo.chef.user;

import com.yolo.chef.dto.LoginRequest;
import com.yolo.chef.dto.UserInfoResponse;
import com.yolo.chef.userProfile.UserProfileRepository;
import com.yolo.chef.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ValidationUtil validationUtil;

    public UserInfoResponse getUserInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> yoloChefAccess = (Map<String, Object>) resourceAccess.get("yolo-chef");
        List<String> roles = (List<String>) yoloChefAccess.get("roles");

        String userCreationMessage = createUserIfNotExists(username, email);

        return new UserInfoResponse(username, email, name, roles, userCreationMessage);
    }

    public String createUserIfNotExists(String username, String email) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return "User already exists";
        }

        LoginRequest loginRequest = new LoginRequest(username, email);
        createUser(loginRequest);
        return "User created successfully";
    }

    public ResponseEntity<Map<String, String>> createUser(LoginRequest loginRequest) {
//        validationUtil.validateNewUser(loginRequest);
        User user = new User();
        user.setUsername(loginRequest.username());
        user.setEmail(loginRequest.email());
        user.setRoleId(1);
        user.setIsDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public boolean checkUserProfileExists(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            long userId = userOptional.get().getId();
            return userProfileRepository.existsByUserId(userId);
        }

        return false;
    }

}
