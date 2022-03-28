package com.features.management;

import com.Types;
import com.TS3.TS3Connection;
import com.TS3.TS3Infos;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class Commands {

    private static Boolean hasRights(Client client) {
        for (int group_id : ActivityDisplay.getAuthorizedGroups()) {
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
