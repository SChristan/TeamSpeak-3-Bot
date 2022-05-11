package com.features.twitchdisplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.MySQL;

import org.json.JSONException;
import org.json.JSONObject;

public class Authentication {

    private static String client_id_;
    private static String client_secret_;
    private static String auth_token_;

    public static String getClientID() {
        return client_id_;
    }

    public static String getToken() {
        return auth_token_;
    }

    public static void loadClientCredentials() {
        try {
            HashMap<String, String> credentials = new HashMap<String, String>();
            ResultSet result = MySQL.getStatement().executeQuery("SELECT * FROM td__authentication");
            while (result.next()) {
                credentials.put(result.getString("id"), result.getString("value"));
            }

            client_id_ = credentials.get("CLIENT_ID");
            client_secret_ = credentials.get("CLIENT_SECRET");

            result.close();
            TwitchDisplay.getLogger().info("Client credentials of the registered Twitch application loaded.");
        } catch (SQLException e) {
            TwitchDisplay.getLogger().error("Database query failed.", e);
        }
    }

    public static void requestToken() {
        try {
            URL url = new URL("https://id.twitch.tv/oauth2/token?client_id=" + client_id_ + "&client_secret=" + client_secret_ + "&grant_type=client_credentials");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            
            BufferedReader response_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response_string = response_reader.readLine();
            auth_token_ = new JSONObject(response_string).get("access_token").toString();

            response_reader.close();
            TwitchDisplay.getLogger().info("Authorization token refreshed.");
        } catch (JSONException | IOException e) {
            TwitchDisplay.getLogger().error("Twitch authentication token request failed.", e);
        }
    }
}
