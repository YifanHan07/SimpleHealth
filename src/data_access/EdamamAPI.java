package data_access;

import entity.NutritionInfo;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * EdamamAPI class for interacting with the Edamam Recipe and Nutrition APIs.
 */
public class EdamamAPI {

    // Constants for Recipe API
    private static final String APP_ID = "6f7c167a";
    private static final String APP_KEY = "9aaac9683d760eaba6e11c57903c20a8";

    // Constants for Nutrition API
    private static final String NUT_APP_ID = "93e3d9f6";
    private static final String NUT_APP_KEY = "b4c03118acc27b97e245fbdac04d7293";

    /**
     * Searches for recipes based on query, max results, and an optional health tag.
     * If tag is "All", the health parameter is omitted.
     *
     * @param query      Search keyword (e.g., "Pasta").
     * @param maxResults Maximum number of recipes to fetch.
     * @param tagQuery        Optional health filter (e.g., "gluten-free"). Use "All" for no filter.
     * @return A list of Recipe objects.
     * @throws Exception If API call fails or response is invalid.
     */
    public static List<Recipe> searchRecipes(String query, int maxResults, StringBuilder tagQuery) throws Exception {
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
    }


    /**
     * Fetches detailed nutrition facts for a given list of ingredients.
     *
     * @param ingredients List of ingredients (e.g., "1 cup rice", "2 chicken breasts").
     * @return A NutritionInfo object containing detailed nutrition facts.
     * @throws Exception If API call fails or response is invalid.
     */
    public static NutritionInfo getNutritionInfo(List<String> ingredients) throws Exception {
        // Construct API URL
        String apiUrl = String.format(
                "https://api.edamam.com/api/nutrition-details?app_id=%s&app_key=%s",
                NUT_APP_ID, NUT_APP_KEY
        );

        // Create JSON payload
        JSONObject payload = new JSONObject();
        payload.put("title", "Nutrition Analysis");
        payload.put("ingr", ingredients);

        // Send POST request
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        os.write(payload.toString().getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject totalNutrients = jsonResponse.getJSONObject("totalNutrients");

            // Extract main nutrients
            int calories = (int) roundToTwoDecimals(getNutrientValue(totalNutrients, "ENERC_KCAL"));
            double fat = roundToTwoDecimals(getNutrientValue(totalNutrients, "FAT"));
            double carbohydrates = roundToTwoDecimals(getNutrientValue(totalNutrients, "CHOCDF"));
            double fiber = roundToTwoDecimals(getNutrientValue(totalNutrients, "FIBTG"));
            double sugar = roundToTwoDecimals(getNutrientValue(totalNutrients, "SUGAR"));

            // Extract additional nutrients (vitamins, minerals, etc.)
            Map<String, Double> additionalNutrients = new HashMap<>();
            for (String key : totalNutrients.keySet()) {
                JSONObject nutrient = totalNutrients.getJSONObject(key);
                String label = nutrient.getString("label");
                double quantity = roundToTwoDecimals(nutrient.getDouble("quantity"));
                String unit = nutrient.getString("unit");
                additionalNutrients.put(label + " (" + unit + ")", quantity);
            }

            // Return a NutritionInfo object with additional nutrients
            return new NutritionInfo(calories, fat, carbohydrates, fiber, sugar, additionalNutrients);

        } else {
            // Handle error response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            in.close();
            throw new Exception("POST request failed. HTTP Code: " + responseCode + ". Error: " + errorResponse);
        }
    }

    /**
     * Helper method to extract a nutrient value from the JSON object.
     *
     * @param totalNutrients The JSON object containing nutrient data.
     * @param nutrientKey    The key for the nutrient (e.g., "ENERC_KCAL").
     * @return The nutrient value as a double, or 0.0 if not found.
     */
    private static double getNutrientValue(JSONObject totalNutrients, String nutrientKey) {
        if (totalNutrients.has(nutrientKey)) {
            JSONObject nutrient = totalNutrients.getJSONObject(nutrientKey);
            return nutrient.getDouble("quantity");
        }
        return 0.0;
    }

    /**
     * Helper method to round a value to two decimal places.
     *
     * @param value The value to round.
     * @return The rounded value.
     */
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
