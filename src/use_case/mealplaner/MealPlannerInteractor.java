package use_case.mealplaner;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MealPlannerInteractor {
    private final List<Recipe> mealPlan;
    private int totalCalories;
    private double totalFat;
    private double totalFiber;
    private double totalSugar;

    public MealPlannerInteractor() {
        this.mealPlan = new ArrayList<>();
        this.totalCalories = 0;
        this.totalFat = 0.0;
        this.totalFiber = 0.0;
        this.totalSugar = 0.0;
    }

    public void addRecipeToMealPlan(Recipe recipe) {
        if (!mealPlan.contains(recipe)) {
            mealPlan.add(recipe);
            totalCalories += recipe.getCalories();
            totalFat = roundToTwoDecimals(totalFat + recipe.getFat());
            totalFiber = roundToTwoDecimals(totalFiber + recipe.getFiber());
            totalSugar = roundToTwoDecimals(totalSugar + recipe.getSugar());
        }
    }

    public void removeRecipeFromMealPlan(Recipe recipe) {
        if (mealPlan.contains(recipe)) {
            mealPlan.remove(recipe);
            totalCalories -= recipe.getCalories();
            totalFat = roundToTwoDecimals(totalFat - recipe.getFat()); // Subtract and round fat
            totalFiber = roundToTwoDecimals(totalFiber - recipe.getFiber()); // Subtract and round fiber
            totalSugar = roundToTwoDecimals(totalSugar - recipe.getSugar()); // Subtract and round sugar
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

    private double roundToTwoDecimals(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
