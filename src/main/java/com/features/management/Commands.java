package com.features.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Types;
import com.TS3.TS3Connection;
import com.TS3.TS3Infos;
import com.bot.BotMain;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class Commands {

    private static List<Integer> authorized_groups_ = new ArrayList<>();

    public static void loadAuthorizedGroups() {
        try {
            authorized_groups_.clear();
            ResultSet result = BotMain.getSQLStatement().executeQuery("SELECT * FROM verified_servergroups");
            while (result.next()) {
                authorized_groups_.add(result.getInt("group_id"));
            }
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
        Client client = TS3Infos.getOnlineClients().get(textEvent.getInvokerId());
        if (textEvent.getMessage().startsWith("!management")) {
            if (hasRights(client)) {
                String[] parts = textEvent.getMessage().split(":");

                if (parts[1].equalsIgnoreCase("reload")) {
                    ActivityDisplay.loadGroups();
                    ActivityDisplay.updateManagerClients();
                    ChannelDescription.update(Types.IS_MANAGER_AND_SUPPORTER);
                } else if (parts[1].equalsIgnoreCase("restart")) {
                    ManagementBot.stop();
                    ManagementBot.start();
                } else {
                    TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "Unknown command!");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
