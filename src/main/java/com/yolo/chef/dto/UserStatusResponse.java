package com.yolo.chef.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusResponse {
    private boolean isUserCreated;
    private boolean isUserProfileCompleted;

    public UserStatusResponse(boolean isUserCreated, boolean isUserProfileCompleted) {
        this.isUserCreated = isUserCreated;
        this.isUserProfileCompleted = isUserProfileCompleted;
    }
}
