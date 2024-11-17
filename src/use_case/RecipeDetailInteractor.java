package use_case;
import data_access.EdamamAPI;
import entites.NutritionInfo;
import entites.RecipeDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDetailInteractor {

    public List<RecipeDetail> execute(List<String> recipeDetails) {
//        List<String> ingredientsList  = new ArrayList<>();
//        List<String> instructionsList  = new ArrayList<>();
//        List<String> tagList  = new ArrayList<>();


        return (List<RecipeDetail>) new RecipeDetail(recipeDetails.get(0), recipeDetails.get(3), recipeDetails.get(4)
                , recipeDetails.get(5));
    }
}
