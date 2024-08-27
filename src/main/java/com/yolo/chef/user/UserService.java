package com.yolo.chef.user;

import com.yolo.chef.dto.LoginRequest;
import com.yolo.chef.dto.UserStatusResponse;
import com.yolo.chef.exception.EmailAlreadyExistsException;
import com.yolo.chef.exception.UsernameAlreadyExistsException;
import com.yolo.chef.utils.ApiMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    public final UserRepository userRepository;
    public UserService(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }
    public ResponseEntity<Map<String, String>> createUser(LoginRequest loginRequest) {
        if (userRepository.existsByUsername(loginRequest.username())) {
            throw new UsernameAlreadyExistsException(ApiMessages.USERNAME_ALREADY_EXISTS_ERROR.getMessage(), "User with username : " + loginRequest.username() + " already exists");
        }

        if (userRepository.existsByEmail(loginRequest.email())) {
            throw new EmailAlreadyExistsException(ApiMessages.USER_EMAIL_ALREADY_EXISTS_ERROR.getMessage(), "User with email : " + loginRequest.email() + " already exists");
        }

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
//    public UserStatusResponse checkUserExistence( String username) {
//       Boolean isUserCreated= userRepository.existsByUsername(username);
//       Boolean isUserProfilecompleted=;
//       return UserStatusResponse(isUserCreated,isUserProfilecompleted);
//    }
}
