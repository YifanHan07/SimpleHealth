import entity.Recipe;
import interface_adapter.mealplaner.MealPlannerController;
import interface_adapter.mealplaner.SaveRecipeController;
import use_case.mealplaner.MealPlannerInteractor;
import use_case.mealplaner.SaveRecipeInteractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MealPlannerAndSaveRecipeTest {

    private MealPlannerController mealPlannerController;
    private SaveRecipeController saveRecipeController;

    @BeforeEach
    public void setUp() {
        // Initialize interactors and controllers
        MealPlannerInteractor mealPlannerInteractor = new MealPlannerInteractor();
        SaveRecipeInteractor saveRecipeInteractor = new SaveRecipeInteractor();

        mealPlannerController = new MealPlannerController(mealPlannerInteractor);
        saveRecipeController = new SaveRecipeController(saveRecipeInteractor);
    }

    @Test
    public void testSaveRecipe() {
        // Create a recipe object
        Recipe recipe = new Recipe(
                "Blackberry + Apple Cocktail",
                "http://www.lottieanddoof.com/2009/09/lottie-doof-kelly-4/",
                222.875829140625,
                0.0,
                0.0,
                0.0,
                List.of("1 cup blackberry", "1 cup apple juice")
        );

        // Save the recipe
        saveRecipeController.saveRecipe(recipe);

        // Assert the recipe is saved
        List<Recipe> savedRecipes = saveRecipeController.getSavedRecipes();
        assertEquals(1, savedRecipes.size());
        assertEquals("Blackberry + Apple Cocktail", savedRecipes.get(0).getLabel());
    }

    @Test
    public void testAddRecipeToMealPlan() {
        // Create a recipe object
        Recipe recipe = new Recipe(
                "Apple Elixir Recipe",
                "http://www.seriouseats.com/apple-elixir-cocktail-recipe.html",
                847.1819270562501,
                0.0,
                0.0,
                0.0,
                List.of("1 cup apple juice", "1/2 cup brandy")
        );

        // Add the recipe to the meal plan
        mealPlannerController.addRecipeToMealPlan(recipe);

        // Assert the recipe is added
        List<Recipe> mealPlan = mealPlannerController.getMealPlan();
        assertEquals(1, mealPlan.size());
        assertEquals("Apple Elixir Recipe", mealPlan.get(0).getLabel());
    }

    @Test
    public void testRemoveRecipeFromMealPlan() {
        // Create and add a recipe object
        Recipe recipe = new Recipe(
                "Apple Elixir Recipe",
                "http://www.seriouseats.com/apple-elixir-cocktail-recipe.html",
                847.1819270562501,
                0.0,
                0.0,
                0.0,
                List.of("1 cup apple juice", "1/2 cup brandy")
        );

        mealPlannerController.addRecipeToMealPlan(recipe);

        // Remove the recipe from the meal plan
        mealPlannerController.removeRecipeFromMealPlan(recipe);

        // Assert the recipe is removed
        List<Recipe> mealPlan = mealPlannerController.getMealPlan();
        assertTrue(mealPlan.isEmpty());
    }

    @Test
    public void testSaveAndRetrieveRecipes() {
        // Create two recipe objects
        Recipe recipe1 = new Recipe(
                "Blackberry + Apple Cocktail",
                "http://www.lottieanddoof.com/2009/09/lottie-doof-kelly-4/",
                222.875829140625,
                1.0,
                1.0,
                10.0,
                List.of("1 cup blackberry", "1 cup apple juice")
        );

        Recipe recipe2 = new Recipe(
                "Apple Elixir Recipe",
                "http://www.seriouseats.com/apple-elixir-cocktail-recipe.html",
                847.1819270562501,
                5.0,
                2.0,
                20.0,
                List.of("1 cup apple juice", "1/2 cup brandy")
        );

        // Save both recipes
        saveRecipeController.saveRecipe(recipe1);
        saveRecipeController.saveRecipe(recipe2);

        // Retrieve and verify saved recipes
        List<Recipe> savedRecipes = saveRecipeController.getSavedRecipes();
        assertEquals(2, savedRecipes.size());
        assertTrue(savedRecipes.stream().anyMatch(r -> r.getLabel().equals("Blackberry + Apple Cocktail")));
        assertTrue(savedRecipes.stream().anyMatch(r -> r.getLabel().equals("Apple Elixir Recipe")));
    }
}
