package com.yolo.chef.recipeRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecipeRequest {
    private String title;
    private String description;
    private Long price;
    private Integer serving_size;
    private String[]  images;
}
