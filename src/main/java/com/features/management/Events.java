package com.features.management;

import com.bot.TS3Connection;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class Events {

    private static TS3Api api_ = TS3Connection.getApi();
    private static TS3EventAdapter event_adapter_;

    public static void startListen() {
        createEventAdapter();
        api_.addTS3Listeners(event_adapter_);
    }

    public static void stopListen() {
        api_.removeTS3Listeners(event_adapter_);
    }

    private static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent textEvent) {
                if (textEvent.getMessage().startsWith("!management")) {
                    String[] parts = textEvent.getMessage().split(":");
                    if (parts[1].equalsIgnoreCase("exit")) {
                        ManagementBot.stop();
                        ManagementBot.getLogger().info("ManagementBot was stopped by the exit command.");
                    }
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {
                ManagementBot.getLogger().info("Client Join Event in ManagementBot was executed.");
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leaveEvent) {
                ManagementBot.getLogger().info("Client Leave Event in ManagementBot was executed.");
            }

            @Override
            public void onClientMoved(ClientMovedEvent movedEvent) {
                ManagementBot.getLogger().info("Client Moved Event in ManagementBot was executed.");
            }
        };
    }
}
