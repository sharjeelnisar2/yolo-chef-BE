package com.yolo.chef.user;

import com.yolo.chef.dto.UserCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/jwtToken")
    public UserInfoResponse getUserInfo(@RequestHeader("Authorization") String token, Authentication authentication) {
        return userService.getUserInfo(authentication);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserCheckResponse> checkUserAndProfile(@PathVariable String username) {
        boolean userExists = userService.userExistsByUsername(username);
        boolean userProfileExists = userExists && userService.checkUserProfileExists(username);

        UserCheckResponse response = new UserCheckResponse(userExists, userProfileExists);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody LoginRequest loginRequest)
            throws Exception {
        return userService.createUser(loginRequest);
    }

}
