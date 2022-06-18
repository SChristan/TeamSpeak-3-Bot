package com.features.twitchdisplay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.MySQL;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.ts3.TS3Connection;
import com.ts3.TS3Infos;

public class Commands {

    private static List<Integer> authorized_groups_ = new ArrayList<>();

    public static void loadAuthorizedGroups() {
        try {
            ResultSet result = MySQL.getStatement().executeQuery("SELECT * FROM td__verified_servergroups");
            authorized_groups_.clear();
            while (result.next()) {
                authorized_groups_.add(result.getInt("group_id"));
            }
            result.close();
        } catch (SQLException e) {
            TwitchDisplay.getLogger().error("Database query failed.", e);
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
        if (parts[0].equalsIgnoreCase("!td")) {
            Client client = TS3Infos.getOnlineClients().get(text_event.getInvokerId());
            if (hasRights(client)) {
                if (parts[1].equalsIgnoreCase("exit")) {
                    TwitchDisplay.stop();
                    TwitchDisplay.getLogger().info("TwitchDisplay was stopped by the exit command.");
                } else if (parts[1].equalsIgnoreCase("reload")) {
                    loadAuthorizedGroups();
                    Authentication.loadClientCredentials();
                    Utility.loadStreamer();
                    TwitchDisplay.getLogger().info("TwitchDisplay was reloaded by the reload command.");
                } else if (parts[1].equalsIgnoreCase("addstreamer")) {
                    if (parts.length == 4 && !parts[2].isEmpty() && !parts[3].isEmpty()) {
                        int errCode = Utility.addStreamer(parts[2], parts[3]);
                        if (errCode == -1) {
                            TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "The streamer was temporarily added but not transferred to the database.");
                        }
                    } else {
                        TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "Invalid input.");
                        TwitchDisplay.getLogger().info("Invalid input at addstreamer command.");
                    }
                } else if (parts[1].equalsIgnoreCase("removestreamer")) {
                    if (parts.length == 3 && !parts[2].isEmpty()) {
                        int errCode = Utility.removeStreamer(parts[2]);
                        if (errCode == -1) {
                            TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "The streamer was temporarily removed but not deleted from the database.");
                        }
                    } else {
                        TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "Invalid input.");
                        TwitchDisplay.getLogger().info("Invalid input at removestreamer command.");
                    }
                } else if (parts[1].equalsIgnoreCase("liststreamer")) {
                    String message = "[B]Streamers:[/B]\n" + String.format("[B]%-21s%s[/B]\n", "user login", "display name");
                    for (Entry<String, StreamerInfo> streamer : Utility.getStreamer().entrySet()) {
                        message = message + String.format("%-30s%s\n", streamer.getValue().getUserLogin(), streamer.getValue().getDisplayName());
                    }
                    TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), message);
                    TwitchDisplay.getLogger().info("Streamer list sent to " + text_event.getInvokerName());
                } else {
                    TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "Unknown command.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(text_event.getInvokerId(), "You are not my master!");
            }
        }
    }
}
