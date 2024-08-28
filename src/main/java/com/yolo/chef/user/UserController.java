package com.yolo.chef.user;

import com.yolo.chef.dto.UserCheckResponse;
import com.yolo.chef.dto.UserInfoResponse;
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
    public ResponseEntity<UserCheckResponse> checkUserAndProfile(@PathVariable String username) {
        boolean userProfileExists =  userService.checkUserProfileExists(username);

        UserCheckResponse response = new UserCheckResponse(userProfileExists);
        return ResponseEntity.ok(response);
    }


}
