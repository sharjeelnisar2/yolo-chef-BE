package com.yolo.chef.userProfile;

import com.yolo.chef.dto.CreateUserProfileRequest;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

 @RestController
 @RequestMapping("/api/v1/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/{username}/userProfiles")
    public ResponseEntity<Map<String, String>> createUserProfile(@PathVariable String username, @RequestBody CreateUserProfileRequest userProfileRq) {
        try {
            return userProfileService.createUserProfile(username,userProfileRq);
        } catch (IllegalArgumentException e) {
            // Handle bad request
            Map<String, String> response = new HashMap<>();
            response.put("message", "Bad Request");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // Handle internal server error
            Map<String, String> response = new HashMap<>();
            response.put("message", "Internal server error");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

