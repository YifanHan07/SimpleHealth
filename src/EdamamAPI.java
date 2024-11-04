import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static void searchRecipes(String query, int maxResults) throws Exception {
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

            // Print each recipe
            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipe = hits.getJSONObject(i).getJSONObject("recipe");
                System.out.println("Recipe " + (i + 1) + ": " + recipe.getString("label"));
                System.out.println("URL: " + recipe.getString("url"));
                System.out.println("Calories: " + recipe.getDouble("calories"));
                System.out.println("Ingredients: " + recipe.getJSONArray("ingredientLines"));
                System.out.println();
            }
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
        }
    }
}
