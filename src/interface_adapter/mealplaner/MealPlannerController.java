package interface_adapter.mealplaner;

import entity.Recipe;
import use_case.mealplaner.MealPlannerInteractor;

import java.util.List;

public class MealPlannerController {
    private final MealPlannerInteractor interactor;

    public MealPlannerController(MealPlannerInteractor interactor) {
        this.interactor = interactor;
    }

    public void addRecipeToMealPlan(Recipe recipe) {
        interactor.addRecipeToMealPlan(recipe);
    }

    public void removeRecipeFromMealPlan(Recipe recipe) {
        interactor.removeRecipeFromMealPlan(recipe);
    }

    public List<Recipe> getMealPlan() {
        return interactor.getMealPlan();
    }

    public int getTotalCalories() {
        return interactor.getTotalCalories();
    }
}
