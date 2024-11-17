package entites;

import java.util.List;

public class RecipeDetail {
    private final String recipeTitle;
    private final List<String> recipeIngredients;
    private final List<String> recipeInstructions;
    private final List<String> recipeTags;


    public RecipeDetail(String recipeTitle, List<String> recipeIngridints, List<String> recipeInstructions, List<String> recipeTags) {
        this.recipeTitle = recipeTitle;
        this.recipeIngredients = recipeIngridints;
        this.recipeInstructions = recipeInstructions;
        this.recipeTags = recipeTags;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public List<String> getRecipeIngredients() {
        return recipeIngredients;
    }

    public List<String> getRecipeDInstructions() {
        return recipeInstructions;
    }

    public List<String> getRecipeTags() {
        return recipeTags;
    }
}
