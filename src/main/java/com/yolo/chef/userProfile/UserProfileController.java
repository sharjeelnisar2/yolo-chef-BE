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
            ResponseEntity<Map<String, String>> response = userProfileService.createUserProfile(username, userProfileRq);
            return response;
    }
}
