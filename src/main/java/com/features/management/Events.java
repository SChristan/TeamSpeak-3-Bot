package com.features.management;

import com.TS3.TS3IDs;
import com.Types;
import com.TS3.TS3Events;
import com.TS3.TS3Infos;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class Events {

    private static TS3EventAdapter event_adapter_;

    public static void startListen() {
        createEventAdapter();
        TS3Events.addListener(event_adapter_);
    }

    public static void stopListen() {
        TS3Events.removeListener(event_adapter_);
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
                if (ActivityDisplay.getManagerClients().containsKey(joinEvent.getUniqueClientIdentifier())) {
                    Client client = TS3Infos.getOnlineClients().get(joinEvent.getClientId());
                    Types activity_status = Types.IS_ONLINE;
                    if (client.getChannelId() == TS3IDs.CHANNEL_ID_AFK_SHORT || client.getChannelId() == TS3IDs.CHANNEL_ID_AFK_LONG) {
                        activity_status = Types.IS_AFK;
                    }
                    ActivityDisplay.getManagerClients().get(joinEvent.getUniqueClientIdentifier()).setActivityStatus(activity_status);
                    ChannelDescription.update(ActivityDisplay.getClientRole(client));

                    ManagementBot.getLogger().info(client.getNickname() + " joined the server.");
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leaveEvent) {
                Client client = TS3Infos.getOnlineClients().get(leaveEvent.getClientId());
                String unique_identifier = client.getUniqueIdentifier();
                if (ActivityDisplay.getManagerClients().containsKey(unique_identifier)) {
                    ActivityDisplay.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_OFFLINE);
                    ChannelDescription.update(ActivityDisplay.getClientRole(client));

                    ManagementBot.getLogger().info(client.getNickname() + " left the server.");
                }
            }

            @Override
            public void onClientMoved(ClientMovedEvent movedEvent) {
                Client client = TS3Infos.getOnlineClients().get(movedEvent.getClientId());
                String unique_identifier = client.getUniqueIdentifier();
                int channel_target_id = movedEvent.getTargetChannelId();
                if (ActivityDisplay.getManagerClients().containsKey(unique_identifier) && (channel_target_id == TS3IDs.CHANNEL_ID_AFK_SHORT || channel_target_id == TS3IDs.CHANNEL_ID_AFK_LONG)) {
                    ActivityDisplay.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_AFK);
                    ChannelDescription.update(ActivityDisplay.getClientRole(client));
					ActivityDisplay.getManagerAFK().add(unique_identifier);

                    ManagementBot.getLogger().info(client.getNickname() + " is now AFK.");
                }

                if (ActivityDisplay.getManagerAFK().contains(unique_identifier) && !(channel_target_id == TS3IDs.CHANNEL_ID_AFK_SHORT || channel_target_id == TS3IDs.CHANNEL_ID_AFK_LONG)) {
					ActivityDisplay.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_ONLINE);
                    ChannelDescription.update(ActivityDisplay.getClientRole(client));
					ActivityDisplay.getManagerAFK().remove(unique_identifier);

                    ManagementBot.getLogger().info(client.getNickname() + " isn't AFK anymore.");
				}
            }
        };
    }
}
