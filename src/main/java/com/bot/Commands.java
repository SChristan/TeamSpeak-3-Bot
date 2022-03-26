package com.bot;

import com.TS3.TS3Connection;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class Commands {

    private static Boolean hasRights() {
        return true;
    }

    public static void execute(TextMessageEvent textEvent) {
        if (textEvent.getMessage().startsWith("!bot")) {
            if (hasRights()) {
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
