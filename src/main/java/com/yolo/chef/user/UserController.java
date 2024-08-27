package com.yolo.chef.user;

import com.yolo.chef.dto.LoginRequest;
import com.yolo.chef.dto.UserStatusResponse;
import com.yolo.chef.recipe.RecipeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private JwtDecoder jwtDecoder;
    public final UserService userService;
    public UserController(UserService userservice)
    {
        this.userService=userservice;
    }
    @PreAuthorize("Chef")
    @GetMapping("/api/v1/jwtToken")
    public ResponseEntity<Map<String, Object>> getUserAccessToken(@RequestHeader("Authorization") String authHeader ) {
        String token = authHeader.substring(7);

        Jwt jwt = jwtDecoder.decode(token);

        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String Username = jwt.getClaimAsString("preferred_username");
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get("vendor123");
        List<String> roles = (List<String>) clientRoles.get("roles");
        Map<String, Object> userDetails = Map.of(
                "name", firstName + " " + lastName,
                "username", Username,
                "email", email,
                "roles", roles
        );

        Map<String, Object> responseBody = Map.of("user_details", userDetails);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);


    }
    @PostMapping("/api/v1/users")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody LoginRequest loginRequest)
            throws Exception {
        return userService.createUser(loginRequest);
    }
//    @PostMapping("/api/v1/users/{username}")
//   public ResponseEntity<UserStatusResponse> checkuserexistence(@PathVariable("username") String username){
//        UserStatusResponse userStatusResponse=userService.checkUserExistence(username);
//        return  userStatusResponse;
//    }


}
