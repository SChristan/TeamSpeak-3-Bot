package com.bot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.ts3.TS3Connection;
import com.ts3.TS3Infos;

public class Commands {

    private static List<Integer> authorized_groups_ = new ArrayList<>();

    public static void loadAuthorizedGroups() {
        try {
            authorized_groups_.clear();
            ResultSet result = BotMain.getSQLStatement().executeQuery("SELECT * FROM verified_servergroups");
            while (result.next()) {
                authorized_groups_.add(result.getInt("group_id"));
            }
            result.close();
        } catch (SQLException e) {
            BotMain.getLogger().error("Exception in Commands loadAuthorizedGroups():", e);
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
        if (parts[0].equalsIgnoreCase("!bot")) {
            Client client = TS3Infos.getOnlineClients().get(textEvent.getInvokerId());
            if (hasRights(client)) {
                if (parts[1].equalsIgnoreCase("exit")) {
                    BotMain.exit();
                } else if (parts[1].equalsIgnoreCase("restart")) {
                    BotMain.stop();
                    BotMain.start();
                } else if (parts[1].equalsIgnoreCase("nickname")) {
                    TS3Connection.getApi().setNickname(parts[2]);
                    BotMain.getLogger().info("The nickname has been updated to: " + parts[2]);
                } else {
                    TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "Unknown command.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
