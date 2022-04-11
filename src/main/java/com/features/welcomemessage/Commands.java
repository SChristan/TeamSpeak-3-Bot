package com.features.welcomemessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bot.BotMain;
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
            BotMain.getLogger().error("Database query failed.", e);
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
        if (parts[0].equalsIgnoreCase("!wm")) {
            Client client = TS3Infos.getOnlineClients().get(textEvent.getInvokerId());
            if (hasRights(client)) {
                if (parts[1].equalsIgnoreCase("exit")) {
                    WelcomeMessage.stop();
                    WelcomeMessage.getLogger().info("WelcomeMessage was stopped by the exit command.");
                } else if (parts[1].equalsIgnoreCase("reload")) {
                    loadAuthorizedGroups();
                    Utility.loadMessage();
                } else {
                    TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "Unknown command.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
