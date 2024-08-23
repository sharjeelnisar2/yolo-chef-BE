package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.recipeImage.RecipeImage;
import com.yolo.chef.recipeImage.RecipeImageRepository;
import com.yolo.chef.recipeStatus.RecipeStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
        if (recipeRequest.getPrice().compareTo(BigInteger.ZERO) <= 0) {
            throw new BadRequestException(
                    "Invalid price provided.",
                    "The price must be greater than zero. Provided value: " + recipeRequest.getPrice()
            );
        }
        Recipe recipe=new Recipe();
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        BigInteger price = recipeRequest.getPrice(); // Assuming this returns a BigInteger
        int multiplier = 100;
        BigInteger multipliedPrice = price.multiply(BigInteger.valueOf(multiplier));
        recipe.setServingSize(recipeRequest.getServing_size());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        //SecurityContextHolder.getContext().getAuthentication().getName();
        recipe.setUserId(1);
        recipe.setIdeaId(ideaId);
        String uniquecode="RCP"+generateUniqueCode();
        recipe.setCode(uniquecode);
        recipe.setRecipeStatusId(1);
        recipeRepository.save(recipe);

        for(int i=0;i<recipeRequest.getImages().length;i++)
        {
            RecipeImage recipeImage=new RecipeImage();
            recipeImage.setUrl(recipeRequest.getImages()[i]);
            System.out.println(recipeRequest.getImages()[i]);
            recipeImage.setCreatedAt(LocalDateTime.now());
            recipeImage.setUpdatedAt(LocalDateTime.now());
            recipeImage.setRecipeId(recipe.getId());
            recipeImageRepository.save(recipeImage)  ;
        }
        return recipe;
    }
}
