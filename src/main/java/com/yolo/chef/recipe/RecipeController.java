package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.exception.UnauthorizedException;
import com.yolo.chef.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class RecipeController {
    public final RecipeService recipeService;
    public RecipeController(RecipeService recipeService)
    {
        this.recipeService=recipeService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CREATE_RECIPE')")
    @PostMapping("/api/v1/ideas/{ideaId}/recipes")
    public ResponseEntity<?> createRecipe(@ModelAttribute RecipeRequest recipeRequest, @PathVariable("ideaId") Integer IdeaId) {

        try {
            Recipe recipe = recipeService.createRecipe(recipeRequest, IdeaId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Recipe created successfully");
            response.put("id",recipe.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (BadRequestException e) {

            throw new BadRequestException(
                    e.getMessage(),
                    e.getDetails());
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(
                    "You are not authorized to create this recipe.",
                    "User does not have the necessary permissions to create a recipe for Idea ID: " + IdeaId
            );
        }  catch (Exception e) {
            String message = "An unexpected error occurred while processing your request.";
            String details = "Error details: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(message, details));
        }
    }
   @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_RECIPE')")
    @PatchMapping("api/v1/recipes/{recipeId}")
    public ResponseEntity<?> updateRecipe(@ModelAttribute RecipeRequest recipeRequest,
                                          @PathVariable("recipeId") Integer recipeId) {

        try {
            Optional<Recipe> updatedRecipe = recipeService.updateRecipe(recipeRequest, recipeId);

            if (updatedRecipe.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Recipe updated successfully");
                return ResponseEntity.ok(response);
            } else {
                throw new BadRequestException(
                        "Invalid recipe ID",
                        "The recipe with ID: " + recipeId + " does not exist."
                );
            }
        } catch (BadRequestException e) {
            throw new BadRequestException(
                    e.getMessage(),
                    e.getDetails()
            );
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(
                    "You are not authorized to update this recipe.",
                    "User does not have the necessary permissions to update a recipe for ID: " + recipeId
            );
        } catch (Exception e) {
            String message = "An unexpected error occurred while processing your request.";
            String details = "Error details: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(message, details));
        }
    }

}
