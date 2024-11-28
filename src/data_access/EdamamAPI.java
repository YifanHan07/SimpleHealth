package data_access;

import entities.NutritionInfo;
import entities.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * EdamamAPI class for interacting with the Edamam Recipe and Nutrition APIs.
 * Provides methods to search recipes and fetch detailed nutrition facts.
 */
public class EdamamAPI {

    private static final String APP_ID = "6f7c167a"; // App ID for Recipe API
    private static final String APP_KEY = "9aaac9683d760eaba6e11c57903c20a8"; // App Key for Recipe API

    private static final String NUT_APP_ID = "93e3d9f6"; // App ID for Nutrition API
    private static final String NUT_APP_KEY = "b4c03118acc27b97e245fbdac04d7293"; // App Key for Nutrition API

    /**
     * Searches for recipes based on a query, max results, and an optional health tag.
     *
     * @param query      The search query (e.g., "Pasta").
     * @param maxResults The maximum number of recipes to fetch.
     * @param tag        An optional health tag filter (e.g., "gluten-free"). Use "All" for no filter.
     * @return A list of Recipe objects.
     * @throws Exception If the API call fails or the response cannot be parsed.
     */
    public static List<Recipe> searchRecipes(String query, int maxResults, String tag) throws Exception {
        // Construct the API URL
        String apiUrl = String.format(
                "https://api.edamam.com/api/recipes/v2?type=public&q=%s&app_id=%s&app_key=%s&from=0&to=%d&health=%s",
                java.net.URLEncoder.encode(query, "UTF-8"), APP_ID, APP_KEY, maxResults,
                tag.equalsIgnoreCase("All") ? "" : tag
        );

        // Send GET request
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the API response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray hits = jsonResponse.getJSONArray("hits");
            List<Recipe> recipeList = new ArrayList<>();

            // Extract recipe details
            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipeJson = hits.getJSONObject(i).getJSONObject("recipe");
                String label = recipeJson.getString("label");
                String urlStr = recipeJson.getString("url");
                double calories = recipeJson.getDouble("calories");
                JSONArray ingredientsArray = recipeJson.getJSONArray("ingredientLines");

                // Convert JSONArray to List<String>
                List<String> ingredientLines = new ArrayList<>();
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    ingredientLines.add(ingredientsArray.getString(j));
                }

                // Create Recipe object and add to the list
                Recipe recipe = new Recipe(label, urlStr, calories, ingredientLines);
                recipeList.add(recipe);
            }
            return recipeList;
        } else {
            // Handle API error response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            in.close();
            throw new Exception("GET request failed. HTTP Code: " + responseCode + ". Error: " + errorResponse);
        }
    }

    /**
     * Fetches detailed nutrition information for a list of ingredients.
     *
     * @param ingredients A list of ingredient strings (e.g., "1 cup rice", "2 chicken breasts").
     * @return A NutritionInfo object containing detailed nutrition facts.
     * @throws Exception If the API call fails or the response cannot be parsed.
     */
    public static NutritionInfo getNutritionInfo(List<String> ingredients) throws Exception {
        // Construct the API URL
        String apiUrl = String.format(
                "https://api.edamam.com/api/nutrition-details?app_id=%s&app_key=%s",
                NUT_APP_ID, NUT_APP_KEY
        );

        // Prepare JSON payload
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

        // Write JSON payload to request body
        OutputStream os = conn.getOutputStream();
        os.write(payload.toString().getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the API response
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

            // Extract key nutrients
            int calories = (int) getNutrientValue(totalNutrients, "ENERC_KCAL"); // Energy in kcal
            double fat = getNutrientValue(totalNutrients, "FAT");               // Fat in grams
            double carbohydrates = getNutrientValue(totalNutrients, "CHOCDF"); // Carbohydrates in grams
            double fiber = getNutrientValue(totalNutrients, "FIBTG");          // Fiber in grams
            double sugar = getNutrientValue(totalNutrients, "SUGAR");          // Sugar in grams

            // Return a NutritionInfo object
            return new NutritionInfo(calories, fat, carbohydrates, fiber, sugar);
        } else {
            // Handle API error response
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
}