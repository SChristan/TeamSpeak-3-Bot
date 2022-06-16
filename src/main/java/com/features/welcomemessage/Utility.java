package com.features.welcomemessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.MySQL;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;

public class Utility {

    private static String welcome_message_;

    public static String getWelcomeMessage(ClientJoinEvent join_event) {
        String message = welcome_message_.replace("%CLIENT_NICKNAME%", join_event.getClientNickname());
        return message;
    }

    public static int setWelcomeMessage(String message) {
        welcome_message_ = message;
        try {
            PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE wm__messages SET message=? WHERE id=?");
            ps.setString(1, message);
            ps.setString(2, "NEWS");
            ps.executeUpdate();
            WelcomeMessage.getLogger().info("Welcome message updated.");
            return 0;
        } catch (SQLException e) {
            WelcomeMessage.getLogger().error("Database query failed.", e);
            return -1;
        }
    }

    public static void loadMessage() {
        try {
            ResultSet result;
            result = MySQL.getStatement().executeQuery("SELECT * FROM wm__messages WHERE id='NEWS'");
            
            while (result.next()) {
                welcome_message_ = result.getString("message");
            }
            result.close();
            
            WelcomeMessage.getLogger().info("Welcome message loaded.");
        } catch (SQLException e) {
            WelcomeMessage.getLogger().error("Database query failed.", e);
        }
    }
}
