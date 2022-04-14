package com.features.welcomemessage;

import com.Types;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.ts3.TS3Connection;
import com.ts3.TS3Events;

public class Events {

    private static TS3EventAdapter event_adapter_;

    public static void startListen() {
        createEventAdapter();
        TS3Events.addListener(event_adapter_, Types.EVENT_TEXT, Types.EVENT_CL_JOIN);
    }

    public static void stopListen() {
        TS3Events.removeListener(event_adapter_);
    }

    private static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent textEvent) {
                Commands.execute(textEvent);
            }

            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {
                if (joinEvent.getClientType() == 0) {
                    TS3Connection.getApi().sendPrivateMessage(joinEvent.getClientId(), Utility.getWelcomeMessage(joinEvent));
                    WelcomeMessage.getLogger().info("Welcome message sent to " + joinEvent.getClientNickname() + " (UUID: " + joinEvent.getUniqueClientIdentifier() + ")");
                }
            }
        };
    }
}
