package data_access;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import entities.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

public class EdamamAPI {

    private static final String APP_ID = "6f7c167a";
    private static final String APP_KEY = "9aaac9683d760eaba6e11c57903c20a8";

    private static final String NUT_APP_ID = "93e3d9f6";
    private static final String NUT_APP_KEY = "b4c03118acc27b97e245fbdac04d7293";

    public static void main(String[] args) {
        try {
            // Test searchRecipes functionality
            System.out.println("Testing Recipe Search:");
            List<Recipe> recipes = searchRecipes("Pasta", 15, "DASH");
            if (recipes != null) {
                for (Recipe recipe : recipes) {
                    System.out.println("Label: " + recipe.getLabel());
                    System.out.println("URL: " + recipe.getUrl());
                    System.out.println("Calories: " + recipe.getCalories());
                    System.out.println("Ingredients: " + recipe.getIngredientLines());
                    System.out.println();
                }
            }

            // Test getNutritionFacts functionality
            System.out.println("Testing Nutrition Facts:");
            List<String> ingredients = new ArrayList<>();
            ingredients.add("1 cup rice");
            ingredients.add("2 chicken breasts");
            ingredients.add("2 cups water");
            ingredients.add("1 tbsp butter");

            List<String> nutritionFacts = getNutritionFacts(ingredients);
            if (nutritionFacts != null) {
                System.out.println("Nutrition Facts:");
                for (String fact : nutritionFacts) {
                    System.out.println(fact);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Recipe> searchRecipes(String query, int maxResults, String tag) throws Exception {
        String apiUrl = String.format(
                "https://api.edamam.com/api/recipes/v2?type=public&q=%s&app_id=%s&app_key=%s&from=0&to=%d&health=%s",
                java.net.URLEncoder.encode(query, "UTF-8"), APP_ID, APP_KEY, maxResults, tag
        );

        // Send GET request
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray hits = jsonResponse.getJSONArray("hits");
            List<Recipe> recipeList = new ArrayList<>();

            // Extract each recipe
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
            System.out.println("GET request failed: HTTP error code " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            in.close();
            System.out.println("Error details: " + errorResponse.toString());
            return null;
        }
    }

    public static List<String> getNutritionFacts(List<String> ingredients) throws Exception {
        String apiUrl = String.format(
                "https://api.edamam.com/api/nutrition-details?app_id=%s&app_key=%s",
                NUT_APP_ID, NUT_APP_KEY
        );

        // Prepare the JSON payload
        JSONObject payload = new JSONObject();
        payload.put("title", "Nutrition Analysis");
        payload.put("ingr", ingredients);

        // Debug: Print the payload
        System.out.println("Payload being sent: " + payload.toString());

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
            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject totalNutrients = jsonResponse.getJSONObject("totalNutrients");
            List<String> nutritionFacts = new ArrayList<>();

            // Extract nutrition facts
            for (String key : totalNutrients.keySet()) {
                JSONObject nutrient = totalNutrients.getJSONObject(key);
                String label = nutrient.getString("label");
                double quantity = nutrient.getDouble("quantity");
                String unit = nutrient.getString("unit");
                nutritionFacts.add(String.format("%s: %.2f %s", label, quantity, unit));
            }

            return nutritionFacts;
        } else {
            System.out.println("POST request failed: HTTP error code " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder errorResponse = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                errorResponse.append(inputLine);
            }
            in.close();
            System.out.println("Error details: " + errorResponse.toString());
            return null;
        }
    }

    // public static List<String> getAllergies(List<String> ingredients) throws Exception {}
}
