package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.recipeImage.RecipeImage;
import com.yolo.chef.recipeImage.RecipeImageRepository;
import com.yolo.chef.recipeStatus.RecipeStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class RecipeService {
    public final RecipeRepository recipeRepository;
    RecipeImageRepository recipeImageRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 5;
    private static final Random RANDOM = new SecureRandom();

    public String generateUniqueCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
    public RecipeService(RecipeRepository recipeRepository,RecipeImageRepository recipeImageRepository)
    {
        this.recipeImageRepository=recipeImageRepository;
        this.recipeRepository=recipeRepository;
    }
    public Recipe createRecipe(RecipeRequest recipeRequest,Integer ideaId)
    {
        if (recipeRequest.getPrice() <= 0) {
            throw new BadRequestException(
                    "Invalid price provided.",
                    "The price must be greater than zero. Provided value: " + recipeRequest.getPrice()
            );
        }
        Recipe recipe=new Recipe();
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setServing_size(recipeRequest.getServing_size());
        recipe.setCreated_at(LocalDateTime.now());
        recipe.setUpdated_at(LocalDateTime.now());
        //SecurityContextHolder.getContext().getAuthentication().getName();
        recipe.setUser_id(1);
        recipe.setIdea_id(ideaId);
        String uniquecode="RCP"+generateUniqueCode();
        recipe.setCode(uniquecode);
        recipe.setRecipe_status_id(1);
        recipeRepository.save(recipe);

        for(int i=0;i<recipeRequest.getImages().length;i++)
        {
            RecipeImage recipeImage=new RecipeImage();
            recipeImage.setUrl(recipeRequest.getImages()[i]);
            System.out.println(recipeRequest.getImages()[i]);
            recipeImage.setCreated_at(LocalDateTime.now());
            recipeImage.setUpdated_at(LocalDateTime.now());
            recipeImage.setRecipe_id(recipe.getId());
            recipeImageRepository.save(recipeImage)  ;
        }
        return recipe;
    }
}
