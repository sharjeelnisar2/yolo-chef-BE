package com.yolo.chef.recipeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Integer> {
    @Query("SELECT r FROM recipe_image r ORDER BY r.id DESC LIMIT 1")
    Optional<RecipeImage> findLastRecordById();
}
