package com.yolo.chef.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private String email;
    private String name;
    private Map<String, Object> roles;

//    public UserInfoResponse(String username, String email, String name, Map<String, Object> roles) {
//    }
}
