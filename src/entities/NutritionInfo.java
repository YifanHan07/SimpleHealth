package entities;

import java.util.Map;

/**
 * NutritionInfo class to store detailed nutrition information for recipes.
 */
public class NutritionInfo {
    private int calories;         // Calories (kcal)
    private double fat;           // Fat (g)
    private double carbohydrates; // Carbohydrates (g)
    private double fiber;         // Fiber (g)
    private double sugar;         // Sugar (g)
    private Map<String, Double> additionalNutrients; // Vitamins and minerals

    /**
     * Constructor to initialize nutrition info.
     *
     * @param calories           Calories in kcal.
     * @param fat                Fat in grams.
     * @param carbohydrates      Carbohydrates in grams.
     * @param fiber              Fiber in grams.
     * @param sugar              Sugar in grams.
     * @param additionalNutrients Map of additional nutrients (e.g., vitamins, minerals).
     */
    public NutritionInfo(int calories, double fat, double carbohydrates, double fiber, double sugar, Map<String, Double> additionalNutrients) {
        this.calories = calories;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
        this.additionalNutrients = additionalNutrients;
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

    public Map<String, Double> getAdditionalNutrients() {
        return additionalNutrients;
    }

    public void setAdditionalNutrients(Map<String, Double> additionalNutrients) {
        this.additionalNutrients = additionalNutrients;
    }

    /**
     * Formats the nutrition info as a string.
     *
     * @return A string representation of the nutrition info.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Calories: %d kcal\n", calories));
        sb.append(String.format("Fat: %.2f g\n", fat));
        sb.append(String.format("Carbohydrates: %.2f g\n", carbohydrates));
        sb.append(String.format("Fiber: %.2f g\n", fiber));
        sb.append(String.format("Sugar: %.2f g\n", sugar));

        if (additionalNutrients != null && !additionalNutrients.isEmpty()) {
            sb.append("Additional Nutrients:\n");
            for (Map.Entry<String, Double> entry : additionalNutrients.entrySet()) {
                sb.append(String.format("%s: %.2f\n", entry.getKey(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}
