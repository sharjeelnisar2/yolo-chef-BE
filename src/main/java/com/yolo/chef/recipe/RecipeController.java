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
@CrossOrigin
@RestController
@RequestMapping("/api/v1")

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
            Map<String, String> response = new HashMap<>();
            response.put("message", "Recipe created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (BadRequestException e) {

            throw new BadRequestException(                      "Invalid request data. Please check your input and try again.",
                    "The provided data for the recipe creation is not valid. Idea ID: " + IdeaId            );
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

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_RECIPES')")
    @GetMapping("/ideas/{idea_id}/recipes")
    public ResponseEntity<RecipeListResponse> getRecipesByIdeaId(
            @PathVariable("idea_id") Integer ideaId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort_order", required = false, defaultValue = "desc") String sortOrder) {

        RecipeListResponse recipeListResponse = recipeService.getAllRecipesByChef(ideaId, status, sortOrder);
        return ResponseEntity.ok(recipeListResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_RECIPE_DETAIL')")
    @GetMapping("/recipes/{recipe_id}")
    public ResponseEntity<RecipeDetailsResponseWrapper> getRecipeDetailsByRecipeId(@PathVariable("recipe_id") Integer recipeId) {

        RecipeDetailsResponseWrapper recipeDetails = recipeService.getRecipeDetailsByRecipeId(recipeId);
        return ResponseEntity.ok(recipeDetails);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_RECIPE_STATUS')")
    @PatchMapping("/recipes/{recipe_id}")
    public ResponseEntity<Map<String, String>> updateRecipeStatus(@PathVariable("recipe_id") Integer recipeId, @RequestBody Map<String, String> requestBody ) throws Exception {
        String status = requestBody.get("status");
        return recipeService.updateRecipeStatus(recipeId, status);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DELETE_RECIPE')")
    @DeleteMapping("/recipes/{recipe_id}")
    public ResponseEntity<Map<String, String>> deleteRecipe(@PathVariable("recipe_id") Integer recipeId ) throws Exception {
        return recipeService.deleteRecipe(recipeId);
    }

}
