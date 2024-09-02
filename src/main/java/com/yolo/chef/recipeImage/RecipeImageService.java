package com.yolo.chef.recipeImage;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeImageService {
    private final RecipeImageRepository recipeImageRepository;

    public List<RecipeImage> findImageByRecipeId(Integer recipeId) {
        return recipeImageRepository.findByRecipeId(recipeId);
    }

    public List<String> getAllUrlAgainstId(Integer recipeId) {
        List<String> storedUrls= recipeImageRepository.findAllUrlsByRecipeId(recipeId);

        return storedUrls;
    }
    public Boolean deleteByUrl(String url)
    {
        Optional<RecipeImage> recipeImageOptional=recipeImageRepository.getIdByUrl(url);
        if(recipeImageOptional.isPresent())
        {

            recipeImageRepository.delete(recipeImageOptional.get());
            return true;
        }
        return false;
    }
}
