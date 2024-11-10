import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomHttpClient {
    public JSONObject get(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString());
        } else {
            handleApiError(conn, responseCode);
            return null;
        }
    }

    private void handleApiError(HttpURLConnection conn, int responseCode) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        String inputLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            errorResponse.append(inputLine);
        }
        in.close();
        System.out.println("Error details: " + errorResponse);
        throw new Exception("API request failed with code: " + responseCode);
    }
} 