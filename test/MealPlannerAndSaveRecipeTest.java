import entity.Recipe;
import interface_adapter.mealplaner.MealPlannerController;
import interface_adapter.mealplaner.SaveRecipeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.mealplaner.MealPlannerInteractor;
import use_case.mealplaner.SaveRecipeInteractor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealPlannerAndSaveRecipeTest {

    private SaveRecipeController saveRecipeController;
    private MealPlannerController mealPlannerController;

    @BeforeEach
    void setUp() {
        SaveRecipeInteractor saveRecipeInteractor = new SaveRecipeInteractor();
        MealPlannerInteractor mealPlannerInteractor = new MealPlannerInteractor();

        saveRecipeController = new SaveRecipeController(saveRecipeInteractor);
        mealPlannerController = new MealPlannerController(mealPlannerInteractor);
    }

    @Test
    void testSaveRecipe() {
        Recipe recipe1 = new Recipe(
                "Apple Pie",
                "http://example.com/apple-pie",
                250.0,
                List.of("2 cups of flour", "1 cup of sugar", "1/2 cup of butter")
        );
        Recipe recipe2 = new Recipe(
                "Banana Bread",
                "http://example.com/banana-bread",
                300.0,
                List.of("3 bananas", "1 cup of sugar", "2 cups of flour")
        );

        // Save recipes
        saveRecipeController.saveRecipe(recipe1);
        saveRecipeController.saveRecipe(recipe2);

        // Check saved recipes
        List<Recipe> savedRecipes = saveRecipeController.getSavedRecipes();
        assertEquals(2, savedRecipes.size());
        assertTrue(savedRecipes.contains(recipe1));
        assertTrue(savedRecipes.contains(recipe2));
    }

    @Test
    void testSaveDuplicateRecipe() {
        Recipe recipe = new Recipe(
                "Apple Pie",
                "http://example.com/apple-pie",
                250.0,
                List.of("2 cups of flour", "1 cup of sugar", "1/2 cup of butter")
        );

        // Save the same recipe twice
        saveRecipeController.saveRecipe(recipe);
        saveRecipeController.saveRecipe(recipe);

        // Check that the recipe is not duplicated
        List<Recipe> savedRecipes = saveRecipeController.getSavedRecipes();
        assertEquals(1, savedRecipes.size());
    }

    @Test
    void testAddRecipeToMealPlan() {
        Recipe recipe1 = new Recipe(
                "Apple Pie",
                "http://example.com/apple-pie",
                250.0,
                List.of("2 cups of flour", "1 cup of sugar", "1/2 cup of butter")
        );
        Recipe recipe2 = new Recipe(
                "Banana Bread",
                "http://example.com/banana-bread",
                300.0,
                List.of("3 bananas", "1 cup of sugar", "2 cups of flour")
        );

        // Add recipes to the meal plan
        mealPlannerController.addRecipeToMealPlan(recipe1);
        mealPlannerController.addRecipeToMealPlan(recipe2);

        // Check meal plan content
        List<Recipe> mealPlan = mealPlannerController.getMealPlan();
        assertEquals(2, mealPlan.size());
        assertTrue(mealPlan.contains(recipe1));
        assertTrue(mealPlan.contains(recipe2));
    }

    @Test
    void testRemoveRecipeFromMealPlan() {
        Recipe recipe = new Recipe(
                "Apple Pie",
                "http://example.com/apple-pie",
                250.0,
                List.of("2 cups of flour", "1 cup of sugar", "1/2 cup of butter")
        );

        // Add and then remove the recipe from the meal plan
        mealPlannerController.addRecipeToMealPlan(recipe);
        mealPlannerController.removeRecipeFromMealPlan(recipe);

        // Check meal plan content
        List<Recipe> mealPlan = mealPlannerController.getMealPlan();
        assertTrue(mealPlan.isEmpty());
    }

    @Test
    void testTotalCaloriesInMealPlan() {
        Recipe recipe1 = new Recipe(
                "Apple Pie",
                "http://example.com/apple-pie",
                250.0,
                List.of("2 cups of flour", "1 cup of sugar", "1/2 cup of butter")
        );
        Recipe recipe2 = new Recipe(
                "Banana Bread",
                "http://example.com/banana-bread",
                300.0,
                List.of("3 bananas", "1 cup of sugar", "2 cups of flour")
        );

        // Add recipes to the meal plan
        mealPlannerController.addRecipeToMealPlan(recipe1);
        mealPlannerController.addRecipeToMealPlan(recipe2);

        // Check total calories
        int totalCalories = mealPlannerController.getTotalCalories();
        assertEquals(550, totalCalories);
    }
}
