package use_case.mealplaner;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerInteractor {
    private final List<Recipe> mealPlan; // List of recipes in the meal plan
    private int totalCalories; // Total calories
    private double totalFat;    // Total fat
    private double totalFiber;  // Total fiber
    private double totalSugar;  // Total sugar

    public MealPlannerInteractor() {
        this.mealPlan = new ArrayList<>();
        this.totalCalories = 0;
        this.totalFat = 0;
        this.totalFiber = 0;
        this.totalSugar = 0;
    }

    public void addRecipeToMealPlan(Recipe recipe) {
        if (!mealPlan.contains(recipe)) {
            mealPlan.add(recipe);
            totalCalories += recipe.getCalories();
            totalFat += recipe.getFat();      // Add fat
            totalFiber += recipe.getFiber();  // Add fiber
            totalSugar += recipe.getSugar();  // Add sugar
        }
    }

    public void removeRecipeFromMealPlan(Recipe recipe) {
        if (mealPlan.contains(recipe)) {
            mealPlan.remove(recipe);
            totalCalories -= recipe.getCalories();
            totalFat -= recipe.getFat();      // Remove fat
            totalFiber -= recipe.getFiber();  // Remove fiber
            totalSugar -= recipe.getSugar();  // Remove sugar
        }
    }

    public List<Recipe> getMealPlan() {
        return new ArrayList<>(mealPlan);
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public double getTotalFiber() {
        return totalFiber;
    }

    public double getTotalSugar() {
        return totalSugar;
    }
}
