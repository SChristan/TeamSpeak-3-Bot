package com.bot;

import com.TS3.TS3Connection;
import com.TS3.TS3Infos;
import com.features.management.ActivityDisplay;
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
        if (textEvent.getMessage().startsWith("!bot")) {
            if (hasRights(client)) {
                String[] parts = textEvent.getMessage().split(":");

                if (parts[1].equalsIgnoreCase("exit")) {
                    BotMain.exit();
                } else if (parts[1].equalsIgnoreCase("restart")) {
                    BotMain.stop();
                    BotMain.start();
                } else if (parts[1].equalsIgnoreCase("nickname")) {
                    TS3Connection.getApi().setNickname(parts[2]);
                    BotMain.getLogger().info("The nickname has been updated.");
                } else {
                    TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "Unknown command!");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
