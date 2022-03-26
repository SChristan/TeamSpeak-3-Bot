package com.bot;

import com.TS3.TS3Connection;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class Commands {

    private static Boolean hasRights() {
        return true;
    }

    public static void execute(TextMessageEvent textEvent) {
        if (textEvent.getMessage().startsWith("!")) {
            if (hasRights()) {
                String[] parts = textEvent.getMessage().split(":");

                if (parts[0].equalsIgnoreCase("!exit")) {
                    BotMain.exit();
                } else if (parts[0].equalsIgnoreCase("!restart")) {
                    BotMain.stop();
                    BotMain.start();
                } else if (parts[0].equalsIgnoreCase("!nickname")) {
                    TS3Connection.getApi().setNickname(parts[1]);
                    BotMain.getLogger().info("The nickname has been updated.");
                }
            } else {
                TS3Connection.getApi().sendPrivateMessage(textEvent.getInvokerId(), "You are not my master!");
            }
        }
    }
}
