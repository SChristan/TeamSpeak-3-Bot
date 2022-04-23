package com.features.activitydisplay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.MySQL;
import com.Types;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.ts3.TS3Connection;
import com.ts3.TS3Infos;

public class Commands {

    private static List<Integer> authorized_groups_ = new ArrayList<>();

    public static void loadAuthorizedGroups() {
        try {
            ResultSet result = MySQL.getStatement().executeQuery("SELECT * FROM ad__verified_servergroups");
            authorized_groups_.clear();
            while (result.next()) {
                authorized_groups_.add(result.getInt("group_id"));
            }
            result.close();
        } catch (SQLException e) {
            ActivityDisplay.getLogger().error("Database query failed.", e);
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

    public static void execute(TextMessageEvent textEvent) {
        String[] parts = textEvent.getMessage().split(":");
        if (parts[0].equalsIgnoreCase("!ad")) {
            Client client = TS3Infos.getOnlineClients().get(textEvent.getInvokerId());
            if (hasRights(client)) {
                if (parts[1].equalsIgnoreCase("exit")) {
                    ActivityDisplay.stop();
                    ActivityDisplay.getLogger().info("ActivityDisplay was stopped by the exit command.");
                } else if (parts[1].equalsIgnoreCase("reload")) {
                    loadAuthorizedGroups();
                    Utility.loadGroups();
                    Utility.updateManagerClients();
                    ChannelDescription.update(Types.IS_MANAGER_AND_SUPPORTER);
                    ActivityDisplay.getLogger().info("ActivityDisplay was reloaded by the reload command.");
                } else {
                    TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "Unknown command.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
