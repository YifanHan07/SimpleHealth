package entities;

import java.util.Map;

/**
 * NutritionInfo class
 * Stores detailed nutritional information for each recipe.
 */
public class NutritionInfo {
    private int calories;         // Calories
    private double fat;           // Fat content (in grams)
    private double carbohydrates; // Carbohydrates content (in grams)
    private double fiber;         // Fiber content (in grams)
    private double sugar;         // Sugar content (in grams)

    /**
     * Constructor
     *
     * @param calories      Calories
     * @param fat           Fat content
     * @param carbohydrates Carbohydrates content
     * @param fiber         Fiber content
     * @param sugar         Sugar content
     */
    public NutritionInfo(int calories, double fat, double carbohydrates, double fiber, double sugar) {
        this.calories = calories;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
    }

    /**
     * Builds a NutritionInfo object from a map of nutrients.
     *
     * @param nutrients A map of nutrient names to their values.
     *                  Keys should include "Energy", "Fat", "Carbohydrate", "Fiber", and "Sugars".
     * @return A NutritionInfo object populated with values from the map.
     */
    public static NutritionInfo fromNutrientsMap(Map<String, Double> nutrients) {
        int calories = nutrients.getOrDefault("Energy", 0.0).intValue(); // Convert Double to int for calories
        double fat = nutrients.getOrDefault("Fat", 0.0);
        double carbohydrates = nutrients.getOrDefault("Carbohydrate", 0.0);
        double fiber = nutrients.getOrDefault("Fiber", 0.0);
        double sugar = nutrients.getOrDefault("Sugars", 0.0);

        return new NutritionInfo(calories, fat, carbohydrates, fiber, sugar);
    }

    // Getters and Setters

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    /**
     * Formats the nutritional information into a readable string.
     *
     * @return A formatted string containing the nutritional information.
     */
    @Override
    public String toString() {
        return String.format(
                "Nutrition Info:\n" +
                        "  Calories: %d kcal\n" +
                        "  Fat: %.2f g\n" +
                        "  Carbohydrates: %.2f g\n" +
                        "  Fiber: %.2f g\n" +
                        "  Sugar: %.2f g\n",
                calories, fat, carbohydrates, fiber, sugar
        );
    }
}
