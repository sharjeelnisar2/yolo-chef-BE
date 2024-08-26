package com.yolo.chef.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @GetMapping("/jwtToken")
    @CrossOrigin(origins = "http://localhost:3002")
    public Map<String, Object> getUserInfo(@RequestHeader("Authorization") String token, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        Map<String, Object> roles = jwt.getClaim("resource_access");

        System.out.println(username);
        return Map.of(
                "username", username,
                "email", email,
                "roles", roles
        );
    }
}
