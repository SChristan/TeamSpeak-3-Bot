package com.features.management;

import com.TS3.TS3Connection;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class Commands {

    private static Boolean hasRights() {
        return true;
    }

    public static void execute(TextMessageEvent textEvent) {
        if (textEvent.getMessage().startsWith("!management")) {
            if (hasRights()) {
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
