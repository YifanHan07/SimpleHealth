import java.util.ArrayList;
import java.util.List;

import entity.RecipeEntity;
import org.json.JSONArray;
import org.json.JSONObject;

public class EdamamAPI {

    private static final String APP_ID = "6f7c167a"; // Replace with your Edamam App ID
    private static final String APP_KEY = "9aaac9683d760eaba6e11c57903c20a8"; // Replace with your Edamam API Key
    private final CustomHttpClient customHttpClient;

    public EdamamAPI(CustomHttpClient httpClient) {
        this.customHttpClient = httpClient;
    }

    public static void main(String[] args) {
        try {
            EdamamAPI edamamAPI = new EdamamAPI(new CustomHttpClient());
            List<RecipeEntity> recipes = edamamAPI.searchRecipes("Pasta", 15);
            System.out.println("Total recipes found: " + recipes.size());
            for (RecipeEntity recipe : recipes) {
                System.out.println(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RecipeEntity> searchRecipes(String query, int maxResults) throws Exception {
        String apiUrl = buildApiUrl(query, maxResults);
        JSONObject jsonResponse = customHttpClient.get(apiUrl);
        return parseRecipes(jsonResponse);
    }

    public String buildApiUrl(String query, int maxResults) throws Exception {
        return String.format(
                "https://api.edamam.com/api/recipes/v2?type=public&q=%s&app_id=%s&app_key=%s&from=0&to=%d",
                java.net.URLEncoder.encode(query, "UTF-8"), APP_ID, APP_KEY, maxResults
        );
    }

    public List<RecipeEntity> parseRecipes(JSONObject jsonResponse) {
        List<RecipeEntity> recipes = new ArrayList<>();
        JSONArray hits = jsonResponse.getJSONArray("hits");

        for (int i = 0; i < hits.length(); i++) {
            JSONObject recipeJson = hits.getJSONObject(i).getJSONObject("recipe");
            RecipeEntity recipe = createRecipeFromJson(recipeJson);
            recipes.add(recipe);
        }

        return recipes;
    }

    public RecipeEntity createRecipeFromJson(JSONObject recipeJson) {
        List<String> ingredients = extractIngredients(recipeJson);
        return new RecipeEntity(
            recipeJson.getString("label"),
            recipeJson.getString("url"),
            recipeJson.getDouble("calories"),
            ingredients
        );
    }

    public List<String> extractIngredients(JSONObject recipeJson) {
        List<String> ingredients = new ArrayList<>();
        JSONArray ingredientsJson = recipeJson.getJSONArray("ingredientLines");
        for (int j = 0; j < ingredientsJson.length(); j++) {
            ingredients.add(ingredientsJson.getString(j));
        }
        return ingredients;
    }

}
