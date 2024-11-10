import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import entity.RecipeEntity;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class EdamamAPITest {

    @Mock
    private CustomHttpClient customHttpClient;

    @InjectMocks
    private EdamamAPI edamamAPI;
    
    private JSONObject mockResponse;

    @Before
    public void setUp() {
        // Create mock API response JSON
        String response = """
            {
                "hits": [
                    {
                        "recipe": {
                            "label": "Test Recipe 1",
                            "ingredientLines": ["ingredient 1", "ingredient 2"],
                            "url": "http://test.com/recipe1",
                            "calories": 500
                        }
                    },
                    {
                        "recipe": {
                            "label": "Test Recipe 2",
                            "ingredientLines": ["ingredient 3", "ingredient 4"],
                            "url": "http://test.com/recipe2",
                            "calories": 300
                        }
                    }
                ]
            }
            """;
        mockResponse = new JSONObject(response);
    }

    
    @Test
    public void testSearchRecipes() throws Exception {
        // Mock HTTP client response
        when(customHttpClient.get(anyString())).thenReturn(mockResponse);

        // Execute test
        List<RecipeEntity> results = edamamAPI.searchRecipes("chicken", 2);
        
        // Verify results
        assertNotNull("Search results should not be null", results);
        assertEquals("Should return 2 recipes", 2, results.size());
        
        // Verify first recipe data
        RecipeEntity firstRecipe = results.get(0);
        assertEquals("Test Recipe 1", firstRecipe.getLabel());
        assertEquals(2, firstRecipe.getIngredientLines().size());
        assertEquals(500, firstRecipe.getCalories(), 0.01);
    }
    
}