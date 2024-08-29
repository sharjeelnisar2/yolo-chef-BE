package com.yolo.chef.geminiApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class GeminiApiController {

    private final GeminiApiService geminiService;

    public GeminiApiController(GeminiApiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generateContent")
    public Map<String, Object> generateContent(@RequestBody AIRequest aiRequest) {
        Map<String, Object> responseMap = new HashMap<>();
        String text="create me a recipe for idea created by customer that has idea title"
                +aiRequest.getAITitle()+"and idea descroiption "+
                aiRequest.getAIDescription()+"and customer tell me that his intrest are "+
                aiRequest.getAIIntrests()+"and his DietaryRestrictions are"+
                aiRequest.getAIDietaryRestrictions()+"so give me a json string form that has RecipeName that suggest me the recipe name(max length is 32) ,RecipeDescription that has recipe how to make recipe and ServingSize that is its serving size(a number) also note that the response should not have extra info like written like "+
                "{RecipeName: Cheesy Butterfly Birthday Cake," +
                "RecipeDescription:This cake is shaped like a butterfly and has a cheesy filling. " +
                "Ingredients:,+" +
                "ServingSize:12}" +
                "``` plz skip the ``` and json word...i just want an json string having 3 attributes also dont have characters like /n and * in response";
        String requestPayload = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", text);


        String aiResponse = geminiService.generateContent(requestPayload);
        System.out.println(aiResponse);
        // Parse AI response and map to desired format
        try {
            // Here, we assume aiResponse is a JSON string containing RecipeName, RecipeDescription, and ServingSize
            ObjectMapper responseMapper = new ObjectMapper();
            Map<String, Object> aiData = responseMapper.readValue(aiResponse, new TypeReference<Map<String, Object>>() {});

            // Populate the responseMap with values from aiData
            responseMap.put("recipeName", aiData.get("RecipeName"));
            responseMap.put("recipedescription", aiData.get("RecipeDescription"));
            responseMap.put("recipeservingSize", aiData.get("ServingSize"));

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", "Failed to parse AI response");
        }
        return responseMap;
    }
}