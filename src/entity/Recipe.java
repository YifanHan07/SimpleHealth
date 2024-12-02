package entity;

import java.util.List;

public class Recipe {
    private String label;
    private String url;
    private double calories;
    private double fat;
    private double fiber;
    private double sugar;
    private List<String> ingredientLines;

    // Constructor
    public Recipe(String label, String url, double calories, double fat, double fiber, double sugar,List<String> ingredientLines) {
        this.label = label;
        this.url = url;
        this.calories = calories;
        this.fat = fat;
        this.fiber = fiber;
        this.sugar = sugar;
        this.ingredientLines = ingredientLines;
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public double getCalories() {
        return calories;
    }

    public double getFat() {
        return fat;
    }

    public double getFiber() {
        return fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }
}
