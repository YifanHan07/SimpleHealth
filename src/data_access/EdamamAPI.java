package data_access;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class EdamamAPI {

    private static final String APP_ID = "6f7c167a"; // Replace with your Edamam App ID
    private static final String APP_KEY = "9aaac9683d760eaba6e11c57903c20a8"; // Replace with your Edamam API Key

    public static void main(String[] args) {
        try {
            searchRecipes("Pasta", 15);
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

            // Print each recipe
            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipe = hits.getJSONObject(i).getJSONObject("recipe");
                List<String> eachRecipe = new ArrayList<>();
                eachRecipe.add("Recipe " + (i + 1) + ": " + recipe.getString("label"));
                eachRecipe.add("URL: " + recipe.getString("url"));
                eachRecipe.add("Calories: " + recipe.getDouble("calories"));

                JSONArray ingredients = recipe.getJSONArray("ingredients");
                StringBuilder ingredient = new StringBuilder();
                for (int j = 0; j < ingredients.length(); j++) {
                    JSONObject ingredientJSON = ingredients.getJSONObject(j);

                    ingredient.append("Text: ").append(ingredientJSON.getString("text"));

                    if (JSONObject.NULL.equals(ingredientJSON.get("quantity"))) {
                        ingredient.append(", Quantity is empty");
                    }
                    else ingredient.append(", Quantity: ").append(ingredientJSON.getDouble("quantity"));

                    if (JSONObject.NULL.equals(ingredientJSON.get("measure"))) {
                        ingredient.append(", Measure is empty");
                    }
                    else ingredient.append(", Measure: ").append(ingredientJSON.getString("measure"));

                    if (JSONObject.NULL.equals(ingredientJSON.get("weight"))) {
                        ingredient.append(", Weight is empty");
                    }
                    else ingredient.append(", Weight: ").append(Math.round(ingredientJSON.getDouble("weight")));

                    if (JSONObject.NULL.equals(ingredientJSON.get("foodId"))) {
                        ingredient.append(", FoodId is empty");
                    }
                    else ingredient.append(", FoodId: ").append(ingredientJSON.getString("foodId"));

                    ingredient.append("\n");

//                    ingredient.append("Text: ").append(ingredientJSON.getString("text"))
//                            .append(", Quantity: ").append(ingredientJSON.getDouble("quantity"))
//                            .append(", Measure: ").append(ingredientJSON.getString("measure"))
//                            .append(", Food: ").append(ingredientJSON.getString("food"))
//                            .append(", Weight: ").append(Math.round(ingredientJSON.getDouble("weight")))
//                            .append(", Food ID: ").append(ingredientJSON.getString("foodId"))
//                            .append("\n");
                }
                eachRecipe.add("Ingredient" + ingredient);
//
//                JSONArray instructions = recipe.getJSONArray("instructions");
//                StringBuilder instruction = new StringBuilder();
//                for (int j = 0; j < instructions.length(); j++) {
//                    instruction.append(j + 1).append(":").append(instructions.getString(j));
//                }
//                eachRecipe.add("Instructions: " + instruction);
//
//                JSONArray tags = recipe.getJSONArray("tags");
//                StringBuilder tag = new StringBuilder();
//                for (int j = 0; j < tags.length(); j++) {
//                    if (j > 0) {
//                        tag.append(" ");
//                    }
//                    tag.append(tags.getString(j));
//                }
//                eachRecipe.add(String.valueOf(tag));
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
}
