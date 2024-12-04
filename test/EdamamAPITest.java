import data_access.EdamamAPI;
import entity.NutritionInfo;
import entity.Recipe;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EdamamAPITest {

    @Test
    void testSearchRecipesValidQueryAndTag() {
        try {
            // Arrange
            String query = "chicken";
            StringBuilder healthTag = new StringBuilder("gluten-free");

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, 25, healthTag);

            // Assert
            assertNotNull(recipes, "The recipe list should not be null");
            assertFalse(recipes.isEmpty(), "The recipe list should not be empty");
            assertTrue(recipes.size() <= 25, "The recipe list should not exceed 25");
            recipes.forEach(recipe -> {
                assertNotNull(recipe.getLabel(), "Recipe label should not be null");
                assertNotNull(recipe.getUrl(), "Recipe URL should not be null");
                assertTrue(recipe.getCalories() >= 0, "Calories should not be negative");
            });
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesValidQueryAndTag: " + e.getMessage());
        }
    }

    @Test
    void testSearchRecipesNoHealthTag() {
        try {
            // Arrange
            String query = "pasta";
            StringBuilder healthTag = new StringBuilder(""); // No health tag

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, 25, healthTag);

            // Assert
            assertNotNull(recipes, "The recipe list should not be null");
            assertFalse(recipes.isEmpty(), "The recipe list should not be empty");
            recipes.forEach(recipe -> {
                assertNotNull(recipe.getLabel(), "Recipe label should not be null");
                assertNotNull(recipe.getUrl(), "Recipe URL should not be null");
            });
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesNoHealthTag: " + e.getMessage());
        }
    }

    @Test
    void testGetNutritionInfoValidIngredients() {
        try {
            // Arrange
            List<String> ingredients = Arrays.asList("1 cup rice", "2 chicken breasts");

            // Act
            NutritionInfo nutritionInfo = EdamamAPI.getNutritionInfo(ingredients);

            // Assert
            assertNotNull(nutritionInfo, "NutritionInfo object should not be null");
            assertTrue(nutritionInfo.getCalories() > 0, "Calories should be greater than 0");
            assertTrue(nutritionInfo.getFat() > 0, "Fat should be greater than 0");
            assertTrue(nutritionInfo.getCarbohydrates() > 0, "Carbohydrates should be greater than 0");
        } catch (Exception e) {
            fail("Exception thrown during testGetNutritionInfoValidIngredients: " + e.getMessage());
        }
    }

    @Test
    void testSearchRecipesInvalidQuery() {
        try {
            // Arrange
            String query = ""; // Invalid query
            StringBuilder healthTag = new StringBuilder("vegan");

            // Act
            List<Recipe> recipes = EdamamAPI.searchRecipes(query, 25, healthTag);

            // Assert
            assertNull(recipes, "The recipe list should not be null");
            assertTrue(recipes.isEmpty(), "The recipe list should be empty for an invalid query");
        } catch (Exception e) {
            fail("Exception thrown during testSearchRecipesInvalidQuery: " + e.getMessage());
        }
    }
}