package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.exception.RecipeNotFoundException;
import com.yolo.chef.exception.RecipeStatusInvalidException;
import com.yolo.chef.idea.IdeaService;
import com.yolo.chef.recipeImage.RecipeImage;
import com.yolo.chef.recipeImage.RecipeImageRepository;
import com.yolo.chef.recipeImage.RecipeImageService;
import com.yolo.chef.recipeStatus.RecipeStatus;
import com.yolo.chef.recipeStatus.RecipeStatusService;
import com.yolo.chef.util.ApiMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IdeaService ideaService;
    private final RecipeImageService recipeImageService;
    private final RecipeStatusService recipeStatusService;
    private final RecipeImageRepository recipeImageRepository;
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
    public Recipe createRecipe(RecipeRequest recipeRequest,Integer ideaId)
    {
        if (recipeRequest.getPrice().compareTo(BigInteger.ZERO) <= 0) {
            throw new BadRequestException(
                    "Invalid price provided.",
                    "The price must be greater than zero. Provided value: " + recipeRequest.getPrice()
            );
        }
        Recipe recipe=new Recipe();
        recipe.setName(recipeRequest.getName());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setPrice( recipeRequest.getPrice().multiply(BigInteger.valueOf(100)));
        recipe.setServingSize(recipeRequest.getServing_size());
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setUpdatedAt(LocalDateTime.now());
        //SecurityContextHolder.getContext().getAuthentication().getName();
        recipe.setUserId(1);
        recipe.setIdeaId(ideaId);
        String uniquecode="RCP"+generateUniqueCode();
        recipe.setCode(uniquecode);
        recipe.setRecipeStatusId(1);
        recipe.setCurrencyId(1);
        recipeRepository.save(recipe);
        for(int i=0;i<recipeRequest.getImages().length;i++)
        {
            RecipeImage recipeImage=new RecipeImage();

            String url=saveImageToStorage(recipeRequest.getImages()[i]);
            recipeImage.setUrl("C://Users/esha.ashfaq/Desktop/RecipeImages/"+recipe.getName()+"/"+url);
            recipeImage.setCreatedAt(LocalDateTime.now());
            recipeImage.setUpdatedAt(LocalDateTime.now());
            recipeImage.setRecipeId(recipe.getId());
            recipeImageRepository.save(recipeImage)  ;
        }
        return recipe;
    }
    public String saveImageToStorage(MultipartFile imageFile) {
        String uploadDirectory = "C://Users/esha.ashfaq/Desktop/RecipeImages";
        String uniqueFileName = UUID.randomUUID().toString() ;

        Path uploadPath = Path.of(uploadDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }

        return uniqueFileName;
    }

    public RecipeListResponse getAllRecipesByChef(Integer ideaId, String status, String sortOrder) {
        List<Recipe> recipes;

        if (status != null) {
            Integer statusId = recipeStatusService.findStatusIdByName(status);
            recipes = recipeRepository.findByUserIdAndIdeaIdAndRecipeStatusId(2, ideaId, statusId);
        } else {
            recipes = recipeRepository.findByUserIdAndIdeaId(2, ideaId);
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            recipes.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));
        } else {
            recipes.sort((r1, r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()));
        }

        return new RecipeListResponse(recipes, ideaService, recipeImageService);
    }


    public RecipeDetailsResponseWrapper getRecipeDetailsByRecipeId(Integer recipeId)
    {
        Optional<Recipe> recipe = recipeRepository.findByUserIdAndId(2, recipeId);
        if(recipe.isPresent())
        {
            return new RecipeDetailsResponseWrapper(recipe.get(), ideaService, recipeImageService, recipeStatusService);
        }
        else {
            throw new RecipeNotFoundException(ApiMessages.RECIPE_NOT_FOUND.getMessage(),"The Recipe Against Recipe Id : " + recipeId +" Not Found" );
        }

    }
    public ResponseEntity<Map<String, String>> updateRecipeStatus(Integer recipeId, String status)
    {
        if(status==null || status.isEmpty())
        {
            throw new RecipeStatusInvalidException(ApiMessages.RECIPE_STATUS_EMPTY_ERROR.getMessage(), "Recipe status cannot be empty" );
        }
        Optional<Recipe> recipe = recipeRepository.findByUserIdAndId(2, recipeId);
        if(recipe.isPresent())
        {
            Integer statusId = recipeStatusService.findStatusIdByName(status);
            if(statusId==null)
            {
                throw new RecipeStatusInvalidException(String.format(ApiMessages.RECIPE_STATUS_INVALID_ERROR.getMessage(), status), "Please give correct status");
            }
            recipe.get().setRecipeStatusId(statusId);
            recipeRepository.save(recipe.get());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe status updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            throw new RecipeNotFoundException(ApiMessages.RECIPE_NOT_FOUND.getMessage(),"The Recipe Against Recipe Id : " + recipeId +" Not Found" );
        }
    }

    public ResponseEntity<Map<String, String>> deleteRecipe(Integer recipeId)
    {
        Integer statusId = recipeStatusService.findStatusIdByName("Draft");
        Optional<Recipe> recipe = recipeRepository.findByUserIdAndIdAndRecipeStatusId(2, recipeId, statusId);
        if(recipe.isPresent())
        {
            statusId = recipeStatusService.findStatusIdByName("Archived");
            recipe.get().setRecipeStatusId(statusId);
            recipeRepository.save(recipe.get());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe deleted successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            throw new RecipeNotFoundException(ApiMessages.RECIPE_NOT_FOUND.getMessage(),"The Recipe Against Recipe Id : " + recipeId +" Not Found" );
        }
    }



}
