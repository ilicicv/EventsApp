package valentina.ilicic.eventsApp;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {

    public static final String BASE_URL = "";

    public interface Callback {
        void onResponse(JSONObject response);
    }

    public static void sendPostRequest(String route, JSONObject jsonBody, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = BASE_URL + route;
                    JSONObject response = postJSONObjectFromURL(urlString, jsonBody);
                    callback.onResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onResponse(null);
                }
            }
        }).start();
    }

    public static void sendGetRequest(String route, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlString = BASE_URL + route;
                    JSONObject response = getJSONObjectFromURL(urlString);
                    callback.onResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onResponse(null);
                }
            }
        }).start();
    }

    private static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            } else {
                return null;
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private static JSONObject postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // [cite: 124]
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            urlConnection.connect();

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonObject.toString()); // [cite: 136]
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode(); // [cite: 139]

            if (responseCode >= 200 && responseCode < 300) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            } else {

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                return new JSONObject(sb.toString());
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}