package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.exception.UnauthorizedException;
import com.yolo.chef.recipeRequest.RecipeRequest;
import com.yolo.chef.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RecipeController {
    public final RecipeService recipeService;
    public RecipeController(RecipeService recipeService)
    {
        this.recipeService=recipeService;
    }

    @PostMapping("/api/v1/ideas/{ideaId}/recipes")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeRequest recipeRequest, @PathVariable("ideaId") Integer IdeaId) {
        try {
            Recipe recipe = recipeService.createRecipe(recipeRequest, IdeaId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (BadRequestException e) {

           throw new BadRequestException(                     "Invalid request data. Please check your input and try again.",
                   "The provided data for the recipe creation is not valid. Idea ID: " + IdeaId            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(
                    "You are not authorized to create this recipe.",
                    "User does not have the necessary permissions to create a recipe for Idea ID: " + IdeaId
            );
        }  catch (Exception e) {
            String message = "An unexpected error occurred while processing your request.";
            String details = "Error details: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", message, details));
        }
    }
}
