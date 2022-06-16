package com.features.welcomemessage;

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

    public static void setWelcomeMessage(String message) {
        welcome_message_ = message;
        try {
            MySQL.getStatement().executeQuery("UPDATE `wm__messages` SET message = " + message + "WHERE id='NEWS'");
            WelcomeMessage.getLogger().info("Welcome message updated.");
        } catch (SQLException e) {
            WelcomeMessage.getLogger().error("Database query failed.", e);
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
