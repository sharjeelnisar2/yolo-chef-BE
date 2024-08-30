package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.recipeImage.RecipeImage;
import com.yolo.chef.recipeImage.RecipeImageRepository;
import com.yolo.chef.recipeStatus.RecipeStatus;
import com.yolo.chef.user.User;
import com.yolo.chef.user.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class RecipeService {
    public final RecipeRepository recipeRepository;
    public final RecipeImageRepository recipeImageRepository;
    public final UserRepository userRepository;
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
    public RecipeService(RecipeRepository recipeRepository, RecipeImageRepository recipeImageRepository, UserRepository userRepository)
    {
        this.recipeImageRepository=recipeImageRepository;
        this.recipeRepository=recipeRepository;
        this.userRepository=userRepository;
    }
    public Integer getRecipeCount(Integer ID)
    {
        List<Recipe> recipesList=recipeRepository.findByIdeaId(ID);
        Integer recipeCount=recipesList.size();
        return recipeCount;
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
        Optional<User> user= userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        recipe.setUserId(user.get().getId());
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
            recipeImage.setUrl(url);
            recipeImage.setCreatedAt(LocalDateTime.now());
            recipeImage.setUpdatedAt(LocalDateTime.now());
            recipeImage.setRecipeId(recipe.getId());
            recipeImageRepository.save(recipeImage)  ;
        }
        return recipe;
    }

    public String saveImageToStorage(MultipartFile imageFile) {
        String uploadDirectory = "C://Users/esha.ashfaq/Desktop/RecipeImages";

        // Get the last recipe number from the database
        String lastImagePath = recipeImageRepository.findLastRecordById()
                .map(RecipeImage::getUrl) // Use getUrl to get the URL field
                .orElse(null); // Return null if no record is found

        int lastRecipeNumber = 0;

        if (lastImagePath != null) {
            // Extract the number from the last image path (e.g., "Recipe3" -> 3)
            String lastRecipeStr = lastImagePath.replaceAll("[^0-9]", "");
            lastRecipeNumber = Integer.parseInt(lastRecipeStr);
        }

        // Increment the recipe number, starting at 1 if no record exists
        int nextRecipeNumber = lastRecipeNumber + 1;

        // Extract the file extension from the original filename
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";

        // Generate the new filename with the recipe number and file extension
        String nextRecipeFilename = "Recipe" + nextRecipeNumber + fileExtension;

        Path uploadPath = Path.of(uploadDirectory);
        Path filePath = uploadPath.resolve(nextRecipeFilename);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }

        return filePath.toString(); // Return the full path or just the filename depending on your need
    }

    public Optional<Recipe> updateRecipe(RecipeRequest recipeRequest,Integer recipeId) {
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
                recipe.setPrice(recipeRequest.getPrice().multiply(BigInteger.valueOf(100)));
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
