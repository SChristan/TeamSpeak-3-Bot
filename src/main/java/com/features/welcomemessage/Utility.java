package com.features.welcomemessage;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;

public class Utility {

    private static String welcome_message_;

    public static String getWelcomeMessage(ClientJoinEvent joinEvent) {
        welcome_message_ = welcome_message_.replace("%CLIENT_NICKNAME%", joinEvent.getClientNickname());
        return welcome_message_;
    }

    public static void loadMessage() {
        try {
            ResultSet result;
            result = WelcomeMessage.getSQLStatement().executeQuery("SELECT * FROM welcome_message WHERE id=1");
            
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
