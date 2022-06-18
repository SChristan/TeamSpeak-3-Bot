package com.features.twitchdisplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.MySQL;
import com.Types;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    private static boolean first_API_attempt_ = true;
    private static LinkedHashMap<String, StreamerInfo> streamer_ = new LinkedHashMap<String, StreamerInfo>();

    public static LinkedHashMap<String, StreamerInfo> getStreamer() {
        return streamer_;
    }

    public static void loadStreamer() {
        try {
            ResultSet result = MySQL.getStatement().executeQuery("SELECT * FROM td__streamer ORDER BY display_name");
            streamer_.clear();
            while (result.next()) {
                String user_login = result.getString("user_login");
                String display_name = result.getString("display_name");

                streamer_.put(user_login, new StreamerInfo(user_login, display_name, Types.TWITCH_OFFLINE));
            }
            result.close();

            TwitchDisplay.getLogger().info("Streamer loaded.");
        } catch (SQLException e) {
            TwitchDisplay.getLogger().error("Database query failed.", e);
        }
    }

    public static int addStreamer(String user_login, String display_name) {
        try {
            Utility.getStreamer().put(user_login, new StreamerInfo(user_login, display_name, Types.TWITCH_OFFLINE));
            PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO td__streamer (user_login, display_name) VALUES (?, ?)");
            ps.setString(1, user_login);
            ps.setString(2, display_name);
            ps.executeUpdate();
			TwitchDisplay.getLogger().info("Added streamer into database: " + user_login + " / " + display_name);
            return 0;
        } catch (SQLException e) {
            TwitchDisplay.getLogger().error("Database query failed.", e);
            return -1;
        }
    }

    public static int removeStreamer(String user_login) {
        try {
            Utility.getStreamer().remove(user_login);
            PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM td__streamer WHERE user_login=?");
            ps.setString(1, user_login);
            ps.executeUpdate();
			TwitchDisplay.getLogger().info("Removed streamer from database: " + user_login);
            return 0;
        } catch (SQLException e) {
            TwitchDisplay.getLogger().error("Database query failed.", e);
            return -1;
        }
    }

    public static JSONObject twitchAPIRequest(String url_string) {
        int response_code = 0;
        try {
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("client-id", Authentication.getClientID());
            connection.setRequestProperty("Authorization", "Bearer " + Authentication.getToken());
            response_code = connection.getResponseCode();

            BufferedReader response_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response_string = response_reader.readLine();
            JSONObject respond = new JSONObject(response_string);

            response_reader.close();
            return respond;
        } catch (JSONException | IOException e) {
            if (response_code == 401 && first_API_attempt_) {
                first_API_attempt_ = false;
                Authentication.requestToken();
                JSONObject response = twitchAPIRequest(url_string);
                first_API_attempt_ = true;
                return response;
            }
            TwitchDisplay.getLogger().error("Twitch API request failed.", e);
            return null;
        }
    }
}
