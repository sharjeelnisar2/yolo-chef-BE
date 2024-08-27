package com.yolo.chef.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiMessages {
    USER_EMAIL_ALREADY_EXISTS_ERROR("Email already exists"),
    USERNAME_ALREADY_EXISTS_ERROR("Username already exists"),
    RECIPE_NOT_FOUND("Recipe does not exist"),
    IDEA_NOT_FOUND("Idea does not exist");
    private final String message;

}
