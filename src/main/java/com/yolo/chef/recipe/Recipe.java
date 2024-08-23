package com.yolo.chef.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private BigInteger price;
    private Integer servingSize;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String code;
    private Integer user_id;
    private Integer idea_id;
    private Integer recipe_status_id;
    private Integer currency_id;
}
