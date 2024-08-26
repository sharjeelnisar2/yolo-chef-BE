package com.yolo.chef.dietaryRestriction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "dietary_restriction")
public class DietaryRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    @Column(name = "idea_id")
    private Integer ideaId;
}
