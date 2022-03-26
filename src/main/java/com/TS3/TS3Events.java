package com.TS3;

import java.util.ArrayList;
import java.util.List;

import com.bot.BotMain;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TS3Events {

    private static TS3Api api_ = TS3Connection.getApi();
    private static TS3EventAdapter event_adapter_;
    private static List<TS3EventAdapter> event_listeners_ = new ArrayList<TS3EventAdapter>();

    public static void addListener(TS3EventAdapter listener) {
        event_listeners_.add(listener);
    }
    
    public static void removeListener(TS3EventAdapter listener) {
        event_listeners_.remove(listener);
    }

    public static void startListen() {
        createEventAdapter();
        api_.addTS3Listeners(event_adapter_);
    }

    public static void stopListen() {
        api_.removeTS3Listeners(event_adapter_);
    }
    public static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent textEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onTextMessage(textEvent);
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {
                if (joinEvent.getClientType() == 0) {
                    Client client = api_.getClientInfo(joinEvent.getClientId());
                    TS3Infos.addOnlineClient(client);
                    BotMain.getLogger().debug("Client went online: " + joinEvent.getClientId() + "  " + joinEvent.getClientNickname());
                }
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onClientJoin(joinEvent);
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leaveEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onClientLeave(leaveEvent);
                }
                TS3Infos.removeOnlineClient(leaveEvent.getClientId());
                BotMain.getLogger().debug("Client went offline: " + leaveEvent.getClientId());
            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onServerEdit(serverEditedEvent);
                }
            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelEdit(channelEditedEvent);
                }
            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelDescriptionChanged(channelDescriptionEditedEvent);
                }
            }

            @Override
            public void onClientMoved(ClientMovedEvent movedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onClientMoved(movedEvent);
                }
            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelCreate(channelCreateEvent);
                }
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelDeleted(channelDeletedEvent);
                }
            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelMoved(channelMovedEvent);
                }
            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onChannelPasswordChanged(channelPasswordChangedEvent);
                }
            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
                for (TS3EventAdapter listener : event_listeners_) {
                    listener.onPrivilegeKeyUsed(privilegeKeyUsedEvent);
                }
            }
        };
    }
}
