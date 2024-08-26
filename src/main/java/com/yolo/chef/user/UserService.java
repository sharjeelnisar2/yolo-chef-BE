package com.yolo.chef.user;


import com.yolo.chef.dto.LoginRequest;
import com.yolo.chef.exception.EmailAlreadyExistsException;
import com.yolo.chef.exception.UsernameAlreadyExistsException;
import com.yolo.chef.util.ApiMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        user.setRole_id(1);
        user.setIs_deleted(false);
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
