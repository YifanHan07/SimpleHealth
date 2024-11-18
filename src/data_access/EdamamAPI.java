package data_access;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

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
            List<List<String>> recipes = searchRecipes("Pasta", 15);
            if (recipes != null) {
                for (List<String> recipe : recipes) {
                    for (String detail : recipe) {
                        System.out.println(detail);
                    }
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

    public static List<List<String>> searchRecipes(String query, int maxResults) throws Exception {
        String apiUrl = String.format(
                "https://api.edamam.com/api/recipes/v2?type=public&q=%s&app_id=%s&app_key=%s&from=0&to=%d",
                java.net.URLEncoder.encode(query, "UTF-8"), APP_ID, APP_KEY, maxResults
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
            List<List<String>> recipeList = new ArrayList<>();

            // Extract each recipe
            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipe = hits.getJSONObject(i).getJSONObject("recipe");
                List<String> eachRecipe = new ArrayList<>();
                eachRecipe.add("Recipe " + (i + 1) + ": " + recipe.getString("label"));
                eachRecipe.add("URL: " + recipe.getString("url"));
                eachRecipe.add("Calories: " + recipe.getDouble("calories"));
                eachRecipe.add("Ingredients: " + recipe.getJSONArray("ingredientLines"));
                recipeList.add(eachRecipe);
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

        // Send POST request
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(payload.toString().getBytes("UTF-8"));

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
}
