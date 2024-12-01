package interface_adapter.mealplaner;

import entity.Recipe;
import use_case.mealplaner.SaveRecipeInteractor;

import java.util.List;

public class SaveRecipeController {
    private final SaveRecipeInteractor interactor;

    public SaveRecipeController(SaveRecipeInteractor interactor) {
        this.interactor = interactor;
    }

    public void saveRecipe(Recipe recipe) {
        interactor.saveRecipe(recipe);
    }

    public List<Recipe> getSavedRecipes() {
        return interactor.getSavedRecipes();
    }
}
