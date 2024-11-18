package use_case;
import data_access.EdamamAPI;
import entites.NutritionInfo;
import entites.RecipeDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeDetailInteractor {

    public static RecipeDetail execute(String keyword, String title) throws Exception {

        List<List<String>> recipes = EdamamAPI.searchRecipes(keyword, 10);
        List<String> recipeDetails = new ArrayList<>();

        for (List<String> recipe : recipes) {
            if (recipe.get(0).equals(title)) {
                recipeDetails.add(recipe.get(0));
                recipeDetails.add(recipe.get(3));
//                recipeDetails.add(recipe.get(4));
//                recipeDetails.add(recipe.get(5));
            }
        }

        return new RecipeDetail(title, recipeDetails.get(1));
    }
}
