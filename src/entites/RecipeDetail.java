package entites;

import java.util.List;

public class RecipeDetail {
    private final String recipeTitle;
    private final String recipeIngredients;
//    private final String recipeInstructions;
//    private final String recipeTags;


    public RecipeDetail(String recipeTitle, String recipeIngridints) {
        this.recipeTitle = recipeTitle;
        this.recipeIngredients = recipeIngridints;
//        this.recipeInstructions = recipeInstructions;
//        this.recipeTags = recipeTags;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public String getFormattedDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(recipeTitle).append("\n")
                .append("Ingredients: ").append(String.join(", ", recipeIngredients)).append("\n");

        return details.toString();
    }

}
