package valentina.ilicic.eventsApp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {

    public static final String BASE_URL = "http://192.168.100.40:3000";
    // 10.0.2.2:3000

    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new java.net.URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        String jsonString = sb.toString();
        int responseCode = urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == HttpURLConnection.HTTP_OK ? new JSONArray(jsonString) : null;
    }

    public JSONObject postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new java.net.URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            urlConnection.disconnect();
            return new JSONObject(sb.toString());
        }
        urlConnection.disconnect();
        return null;
    }

    public JSONObject putJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new java.net.URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("PUT");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            urlConnection.disconnect();
            return new JSONObject(sb.toString());
        }
        urlConnection.disconnect();
        return null;
    }
}