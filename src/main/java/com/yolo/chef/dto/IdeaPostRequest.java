package com.yolo.chef.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yolo.chef.dietaryRestriction.DietaryRestriction;
import com.yolo.chef.interest.Interest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter

@AllArgsConstructor
@NoArgsConstructor
public class IdeaPostRequest {
    private Idea idea;
    @Getter
    @Setter
    public static class Idea {
        @JsonProperty("customer_name")
        private String customerName;
        private String title;
        private String description;
        @JsonProperty("idea_code")
        private String ideaCode;
        private List<String> interests;
        @JsonProperty("dietry_restrictions")
        private List<String> dietaryRestrictions;

    }
}




