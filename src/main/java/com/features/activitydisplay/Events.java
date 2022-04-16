package com.features.activitydisplay;

import com.Types;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.ts3.TS3Events;
import com.ts3.TS3IDs;
import com.ts3.TS3Infos;

public class Events {

    private static TS3EventAdapter event_adapter_;

    public static void startListen() {
        createEventAdapter();
        TS3Events.addListener(event_adapter_, Types.EVENT_TEXT, Types.EVENT_CL_JOIN, Types.EVENT_CL_LEAVE, Types.EVENT_CL_MOVED);
    }

    public static void stopListen() {
        TS3Events.removeListener(event_adapter_);
    }

    private static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent text_event) {
                Commands.execute(text_event);
            }

            @Override
            public void onClientJoin(ClientJoinEvent join_event) {
                if (Utility.getManagerClients().containsKey(join_event.getUniqueClientIdentifier())) {
                    Client client = TS3Infos.getOnlineClients().get(join_event.getClientId());
                    Types activity_status = Types.IS_ONLINE;
                    if (client.getChannelId() == TS3IDs.CHANNEL_ID_AFK_SHORT || client.getChannelId() == TS3IDs.CHANNEL_ID_AFK_LONG) {
                        activity_status = Types.IS_AFK;
                    }
                    Utility.getManagerClients().get(join_event.getUniqueClientIdentifier()).setActivityStatus(activity_status);
                    ChannelDescription.update(Utility.getClientRole(client));

                    ActivityDisplay.getLogger().info(client.getNickname() + " joined the server.");
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leave_event) {
                if (TS3Infos.getOnlineClients().containsKey(leave_event.getClientId())) {
                    Client client = TS3Infos.getOnlineClients().get(leave_event.getClientId());
                    String unique_identifier = client.getUniqueIdentifier();
                    if (Utility.getManagerClients().containsKey(unique_identifier)) {
                        Utility.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_OFFLINE);
                        ChannelDescription.update(Utility.getClientRole(client));

                        ActivityDisplay.getLogger().info(client.getNickname() + " left the server.");
                    }
                }
            }

            @Override
            public void onClientMoved(ClientMovedEvent moved_event) {
                Client client = TS3Infos.getOnlineClients().get(moved_event.getClientId());
                String unique_identifier = client.getUniqueIdentifier();
                int channel_target_id = moved_event.getTargetChannelId();
                if (Utility.getManagerClients().containsKey(unique_identifier) && (channel_target_id == TS3IDs.CHANNEL_ID_AFK_SHORT || channel_target_id == TS3IDs.CHANNEL_ID_AFK_LONG)) {
                    Utility.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_AFK);
                    ChannelDescription.update(Utility.getClientRole(client));
					Utility.getManagerAFK().add(unique_identifier);

                    ActivityDisplay.getLogger().info(client.getNickname() + " is now AFK.");
                }

                if (Utility.getManagerAFK().contains(unique_identifier) && !(channel_target_id == TS3IDs.CHANNEL_ID_AFK_SHORT || channel_target_id == TS3IDs.CHANNEL_ID_AFK_LONG)) {
					Utility.getManagerClients().get(unique_identifier).setActivityStatus(Types.IS_ONLINE);
                    ChannelDescription.update(Utility.getClientRole(client));
					Utility.getManagerAFK().remove(unique_identifier);

                    ActivityDisplay.getLogger().info(client.getNickname() + " isn't AFK anymore.");
				}
            }
        };
    }
}
