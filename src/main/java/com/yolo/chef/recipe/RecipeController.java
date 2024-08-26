package com.yolo.chef.recipe;

import com.yolo.chef.exception.BadRequestException;
import com.yolo.chef.exception.UnauthorizedException;
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

}
