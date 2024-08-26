package com.yolo.chef.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CheckUserResponse> checkUser(@PathVariable String username) {
        CheckUserResponse response = userService.checkUser(username);
        if (!response.isUserCreated()) {
            // Create the user if it does not exist
//            userService.createUser(username);
            System.out.println("User profile doesnot exist");
            // Update response after user creation
            response = userService.checkUser(username);
        }
        return ResponseEntity.ok(response);
    }
}
