package entity;

import java.util.List;

public class RecipeEntity {
    private String label;
    private String url;
    private double calories;
    private List<String> ingredientLines;

    public RecipeEntity(String label, String url, double calories, List<String> ingredientLines) {
        this.label = label;
        this.url = url;
        this.calories = calories;
        this.ingredientLines = ingredientLines;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    @Override
    public String toString() {
        return "RecipeEntity{" +
                "name='" + label + '\'' +
                ", url='" + url + '\'' +
                ", calories=" + calories +
                ", ingredients=" + ingredientLines +
                '}';
    }
}
