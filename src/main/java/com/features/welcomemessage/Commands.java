package com.features.welcomemessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.MySQL;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.ts3.TS3Connection;
import com.ts3.TS3Infos;

public class Commands {

    private static List<Integer> authorized_groups_ = new ArrayList<>();

    public static void loadAuthorizedGroups() {
        try {
            ResultSet result = MySQL.getStatement().executeQuery("SELECT * FROM wm__verified_servergroups");
            authorized_groups_.clear();
            while (result.next()) {
                authorized_groups_.add(result.getInt("group_id"));
            }
            result.close();
        } catch (SQLException e) {
            WelcomeMessage.getLogger().error("Database query failed.", e);
        }
    }

    private static Boolean hasRights(Client client) {
        for (int group_id : authorized_groups_) {
            if (client.isInServerGroup(group_id)) {
                return true;
            }
        }
        return false;
    }

    public static void execute(TextMessageEvent text_event) {
        String[] parts = text_event.getMessage().split(":");
        if (parts[0].equalsIgnoreCase("!wm")) {
            Client client = TS3Infos.getOnlineClients().get(text_event.getInvokerId());
            if (hasRights(client)) {
                if (parts[1].equalsIgnoreCase("exit")) {
                    WelcomeMessage.stop();
                    WelcomeMessage.getLogger().info("WelcomeMessage was stopped by the exit command.");
                } else if (parts[1].equalsIgnoreCase("reload")) {
                    loadAuthorizedGroups();
                    int errCode = Utility.loadMessage();
                    if (errCode == -1) {
                        TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "The welcome wasn't loaded from the database.");
                    }
                } else if (parts[1].equalsIgnoreCase("message")) {
                    parts = text_event.getMessage().split(":", 3);
                    int errCode = Utility.setWelcomeMessage(parts[2]);
                    if (errCode == -1) {
                        TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "The welcome message was temporarily updated but not transferred to the database.");
                    }
                } else {
                    TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "Unknown command.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "You are not my master!");
            }
        }
    }
}
