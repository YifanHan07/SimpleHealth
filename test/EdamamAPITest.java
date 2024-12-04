import data_access.EdamamAPI;
import entity.NutritionInfo;
import entity.Recipe;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EdamamAPITest {

    @Test
    void testSearchRecipesWithLargeQuery() {
        try {
            // Arrange
            String query = "a very long and unnecessarily complicated search term";
            StringBuilder healthTag = new StringBuilder("high-protein");

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, 10, healthTag);

            // Assert
            assertNotNull(recipes, "The recipe list should not be null");
            assertTrue(recipes.isEmpty(), "The recipe list should be empty for such a specific query");
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesWithLargeQuery: " + e.getMessage());
        }
    }

    @Test
    void testGetNutritionInfoWithEmptyIngredients() {
        try {
            // Arrange
            List<String> ingredients = Arrays.asList(); // Empty ingredient list

            // Act
            NutritionInfo nutritionInfo = EdamamAPI.getNutritionInfo(ingredients);

            // Assert
            assertNotNull(nutritionInfo, "NutritionInfo object should not be null");
            assertEquals(0, nutritionInfo.getCalories(), "Calories should be zero for empty ingredients"); // Intentional error: Missing handling for empty list
        } catch (Exception e) {
            fail("Exception thrown during testGetNutritionInfoWithEmptyIngredients: " + e.getMessage());
        }
    }

    @Test
    void testSearchRecipesSpecialCharactersInQuery() {
        try {
            // Arrange
            String query = "@#%$!";
            StringBuilder healthTag = new StringBuilder("");

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, 15, healthTag);

            // Assert
            assertNotNull(recipes, "The recipe list should not be null");
            assertTrue(recipes.isEmpty(), "The recipe list should be empty for a query with special characters");
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesSpecialCharactersInQuery: " + e.getMessage());
        }
    }

    @Test
    void testSearchRecipesWithNegativeLimit() {
        try {
            // Arrange
            String query = "salad";
            StringBuilder healthTag = new StringBuilder("low-fat");

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, -5, healthTag);

            // Assert
            assertNotNull(recipes, "The recipe list should not be null");
            assertTrue(recipes.size() <= 0, "The recipe list should be empty for a negative limit"); // Intentional logic error
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesWithNegativeLimit: " + e.getMessage());
        }
    }

    @Test
    void testGetNutritionInfoWithNullIngredients() {
        try {
            // Arrange
            List<String> ingredients = null; // Null ingredient list

            // Act
            NutritionInfo nutritionInfo = EdamamAPI.getNutritionInfo(ingredients);

            // Assert
            assertNotNull(nutritionInfo, "NutritionInfo object should not be null");
            assertTrue(nutritionInfo.getCalories() == 0, "Calories should be zero for null ingredients");
        } catch (Exception e) {
            fail("Exception thrown during testGetNutritionInfoWithNullIngredients: " + e.getMessage()); // Intentional syntax error: Missing semicolon
        }
    }
}