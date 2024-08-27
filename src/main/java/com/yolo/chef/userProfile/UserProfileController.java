package com.yolo.chef.userProfile;

import com.yolo.chef.dto.CreateUserProfileRequest;
import com.yolo.chef.exception.NotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/{username}/userProfiles")
    public ResponseEntity<Map<String, String>> createUserProfile(@PathVariable String username, @RequestBody CreateUserProfileRequest userProfileRq) {
        try {
            ResponseEntity<Map<String, String>> response = userProfileService.createUserProfile(username, userProfileRq);
            logger.info("Response: {}", response);
            return response;
        } catch (NotFoundException e) {
            logger.error("Error: Username not exists", e);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Username not exists");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error: Bad request", e);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Bad Request");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Internal server error", e);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Internal server error");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
