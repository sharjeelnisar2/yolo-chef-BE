package com.yolo.chef.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/jwtToken")
    @CrossOrigin(origins = "http://localhost:3002")
    public ResponseEntity<UserDetails> checkUser(@AuthenticationPrincipal Jwt jwt) {
        // Extract details from JWT
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        System.out.println(name);

        // Extract roles safely
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            resourceAccess = Map.of(); // Provide an empty map if resource_access is null
        }

        Map<String, Object> apiAccess = (Map<String, Object>) resourceAccess.get("yolo-chef");
        if (apiAccess == null) {
            apiAccess = Map.of(); // Provide an empty map if "alibou-rest-api" key is missing
        }

        List<String> roles = Optional.ofNullable((List<String>) apiAccess.get("roles"))
                .orElse(List.of()); // Default to an empty list if "roles" is missing

        // Create UserDetails object
        UserDetails userDetails = new UserDetails(username, email, roles, name); // Replace with actual logic if needed

        return ResponseEntity.ok(userDetails);
    }
}