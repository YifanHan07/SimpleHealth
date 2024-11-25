package entites;

import java.util.List;

public class Recipe {
    private String label;
    private String url;
    private double calories;
    private List<String> ingredientLines;

    // Constructor
    public Recipe(String label, String url, double calories, List<String> ingredientLines) {
        this.label = label;
        this.url = url;
        this.calories = calories;
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

    public List<String> getIngredientLines() {
        return ingredientLines;
    }
}
