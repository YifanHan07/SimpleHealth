package use_case.mealplaner;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerInteractor {
    private final List<Recipe> mealPlan;
    private int totalCalories;

    public MealPlannerInteractor() {
        this.mealPlan = new ArrayList<>();
        this.totalCalories = 0;
    }

    public void addRecipeToMealPlan(Recipe recipe) {
        if (!mealPlan.contains(recipe)) {
            mealPlan.add(recipe);
            totalCalories += recipe.getCalories();
        }
    }

    public void removeRecipeFromMealPlan(Recipe recipe) {
        if (mealPlan.contains(recipe)) {
            mealPlan.remove(recipe);
            totalCalories -= recipe.getCalories();
        }
    }

    public List<Recipe> getMealPlan() {
        return new ArrayList<>(mealPlan);
    }

    public int getTotalCalories() {
        return totalCalories;
    }
}
