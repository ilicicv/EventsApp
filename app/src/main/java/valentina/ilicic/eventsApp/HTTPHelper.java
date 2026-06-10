package valentina.ilicic.eventsApp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {

    private static final String TAG = "HTTPHelper";
    public static final String BASE_URL = "http://192.168.122.3:3000";

    public JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return new JSONArray(sb.toString());
            }
            return null;
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException e) {}
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public JSONObject postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "POST Response Code za URL [" + urlString + "] je: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONObject(sb.toString());
            }
            return null;
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException e) {}
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public JSONObject putJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        try {
            java.net.URL url = new java.net.URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "PUT Response Code za URL [" + urlString + "] je: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONObject(sb.toString());
            }
            return null;
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException e) {}
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}