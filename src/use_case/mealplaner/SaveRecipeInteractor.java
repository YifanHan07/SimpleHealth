package use_case.mealplaner;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SaveRecipeInteractor {
    private final List<Recipe> savedRecipes;

    public SaveRecipeInteractor() {
        this.savedRecipes = new ArrayList<>();
    }

    public void saveRecipe(Recipe recipe) {
        System.out.println("Attempting to save recipe: " + recipe.getLabel());
        if (!savedRecipes.contains(recipe)) {
            savedRecipes.add(recipe);
            System.out.println("Recipe saved: " + recipe.getLabel());
        } else {
            System.out.println("Recipe already exists: " + recipe.getLabel());
        }
    }

    public List<Recipe> getSavedRecipes() {
        return new ArrayList<>(savedRecipes);
    }
}
