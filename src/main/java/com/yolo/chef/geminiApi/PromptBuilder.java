package com.yolo.chef.geminiApi;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
@Component
@AllArgsConstructor
public class PromptBuilder {
    public String buildPrompt(AIRequest aiRequest) {
        return "create me a recipe for idea created by customer that has idea title"
                +aiRequest.getAITitle()+"and idea descroiption "+
                aiRequest.getAIDescription()+"and customer tell me that his intrest are "+
                aiRequest.getAIIntrests()+"and his DietaryRestrictions are"+
                aiRequest.getAIDietaryRestrictions()+"so give me a json string form that has RecipeName that suggest me the recipe name(max length is 32) ,RecipeDescription that has recipe how to make recipe and ServingSize that is its serving size(a number) also note that the response should not have extra info like written like "+
                "{RecipeName: Cheesy Butterfly Birthday Cake," +
                "RecipeDescription:This cake is shaped like a butterfly and has a cheesy filling. " +
                "Ingredients:,+" +
                "ServingSize:12}" +
                "``` plz skip the ``` and json word...i just want an json string having 3 attributes also dont have characters like /n and * in response";
    }
}
