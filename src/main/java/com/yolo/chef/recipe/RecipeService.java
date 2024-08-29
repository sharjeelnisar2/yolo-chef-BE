package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.recipeImage.RecipeImage;
import com.yolo.chef.recipeImage.RecipeImageRepository;
import com.yolo.chef.recipeStatus.RecipeStatus;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
        if (recipeRequest.getServing_size()<= 0) {
            throw new BadRequestException(
                    "Invalid Serving provided.",
                    "The serving size must be greater than zero. Provided value: " + recipeRequest.getServing_size()
            );
        }
        if (recipeRequest.getImages() == null || recipeRequest.getImages().length == 0)
        {
            throw new BadRequestException(
                    "Invalid Images ",
                    "The Recipe must have atleast 1 image"
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
    public Optional<Recipe> updateRecipe(RecipeRequest recipeRequest,Integer recipeId) {
        System.out.println(recipeId);
        if (recipeRequest.getPrice().compareTo(BigInteger.ZERO) <= 0) {
            throw new BadRequestException(
                    "Invalid price provided.",
                    "The price must be greater than zero. Provided value: " + recipeRequest.getPrice()
            );
        }
        if (recipeRequest.getServing_size()<= 0) {
            throw new BadRequestException(
                    "Invalid Serving provided.",
                    "The serving size must be greater than zero. Provided value: " + recipeRequest.getServing_size()
            );
        }
        Optional<Recipe> existing=recipeRepository.findById(recipeId);
        if (existing.isPresent()) {
            Recipe recipe=existing.get();
            if(recipeRequest.getName()!= null && !recipeRequest.getName().isEmpty())
            {
                recipe.setName(recipeRequest.getName());
            }
            if (recipeRequest.getDescription() != null && !recipeRequest.getDescription().isEmpty()) {
                recipe.setDescription(recipeRequest.getDescription());
            }
            if (recipeRequest.getPrice() != null) {
                recipe.setPrice(recipeRequest.getPrice());
            }
            if (recipeRequest.getServing_size() != null) {
                recipe.setServingSize(recipeRequest.getServing_size());
            }
            recipe.setUpdatedAt(LocalDateTime.now());
            recipeRepository.save(recipe);
            return Optional.of(recipe);
        }
        else {
            return Optional.empty();
        }

    }

    }
