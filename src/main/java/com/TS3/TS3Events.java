package com.ts3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.Types;
import com.bot.BotMain;
import com.bot.Commands;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TS3Events {

    private static TS3Api api_ = TS3Connection.getApi();
    private static TS3EventAdapter event_adapter_;
    private static HashMap<Types, List<TS3EventAdapter>> event_listeners_ = new HashMap<Types, List<TS3EventAdapter>>();
    private static List<Types> ts3_event_types = Arrays.asList(Types.EVENT_TEXT, Types.EVENT_CL_JOIN, Types.EVENT_CL_LEAVE, Types.EVENT_SE_EDIT, Types.EVENT_CH_EDIT, Types.EVENT_CH_DESCRIPTION, Types.EVENT_CL_MOVED, Types.EVENT_CH_CREATE, Types.EVENT_CH_DELETE, Types.EVENT_CH_MOVED, Types.EVENT_CH_PASSWORD, Types.EVENT_PRIV_KEY);

    public static void addListener(TS3EventAdapter listener, Types... types) {
        for (Types type : types) {
            event_listeners_.get(type).add(listener);
        }
    }

    public static void removeListener(TS3EventAdapter listener) {
        for (Types type : ts3_event_types) {
            event_listeners_.get(type).remove(listener);
        }
    }

    public static void startListen() {
        initializeEventTopics();
        createEventAdapter();
        api_.addTS3Listeners(event_adapter_);
    }

    public static void stopListen() {
        api_.removeTS3Listeners(event_adapter_);
    }

    private static void initializeEventTopics() {
        event_listeners_.clear();
        for (Types type : ts3_event_types) {
            event_listeners_.put(type, new ArrayList<TS3EventAdapter>());
        }
    }

    private static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent textEvent) {
                if (textEvent.getInvokerId() != TS3Connection.whoAmI().getId()) {
                    BotMain.getLogger().info(textEvent.getTargetMode() + " message from " + textEvent.getInvokerName() + " (UUID: " + textEvent.getInvokerUniqueId() + "): " + textEvent.getMessage());
                    Commands.execute(textEvent);
                    for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_TEXT)) {
                        listener.onTextMessage(textEvent);
                    }
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {
                BotMain.getLogger().debug("Client went online: " + joinEvent.getClientId() + "  " + joinEvent.getClientNickname());
                if (joinEvent.getClientType() == 0) {
                    Client client = api_.getClientInfo(joinEvent.getClientId());
                    TS3Infos.addOnlineClient(client);
                }
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_JOIN)) {
                    listener.onClientJoin(joinEvent);
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leaveEvent) {
                BotMain.getLogger().debug("Client went offline: " + leaveEvent.getClientId());
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_LEAVE)) {
                    listener.onClientLeave(leaveEvent);
                }
                TS3Infos.removeOnlineClient(leaveEvent.getClientId());
            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_SE_EDIT)) {
                    listener.onServerEdit(serverEditedEvent);
                }
            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_EDIT)) {
                    listener.onChannelEdit(channelEditedEvent);
                }
            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_DESCRIPTION)) {
                    listener.onChannelDescriptionChanged(channelDescriptionEditedEvent);
                }
            }

            @Override
            public void onClientMoved(ClientMovedEvent movedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_MOVED)) {
                    listener.onClientMoved(movedEvent);
                }
            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_CREATE)) {
                    listener.onChannelCreate(channelCreateEvent);
                }
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_DELETE)) {
                    listener.onChannelDeleted(channelDeletedEvent);
                }
            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_MOVED)) {
                    listener.onChannelMoved(channelMovedEvent);
                }
            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_PASSWORD)) {
                    listener.onChannelPasswordChanged(channelPasswordChangedEvent);
                }
            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_PRIV_KEY)) {
                    listener.onPrivilegeKeyUsed(privilegeKeyUsedEvent);
                }
            }
        };
    }
}
