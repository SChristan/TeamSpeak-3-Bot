package com.ts3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.Types;
import com.bot.BotMain;
import com.bot.Commands;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TS3Events {

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
        TS3Connection.getApi().addTS3Listeners(event_adapter_);
    }

    public static void stopListen() {
        TS3Connection.getApi().removeTS3Listeners(event_adapter_);
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
            public void onTextMessage(TextMessageEvent text_event) {
                if (text_event.getInvokerId() != TS3Connection.whoAmI().getId()) {
                    BotMain.getLogger().info(text_event.getTargetMode() + " message from " + text_event.getInvokerName() + " (UUID: " + text_event.getInvokerUniqueId() + "): " + text_event.getMessage());
                    Commands.execute(text_event);
                    for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_TEXT)) {
                        listener.onTextMessage(text_event);
                    }
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent join_event) {
                BotMain.getLogger().debug("Client went online: " + join_event.getClientId() + "  " + join_event.getClientNickname());
                if (join_event.getClientType() == 0) {
                    Client client = TS3Connection.getApi().getClientInfo(join_event.getClientId());
                    TS3Infos.addOnlineClient(client);
                }
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_JOIN)) {
                    listener.onClientJoin(join_event);
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent leave_event) {
                BotMain.getLogger().debug("Client went offline: " + leave_event.getClientId());
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_LEAVE)) {
                    listener.onClientLeave(leave_event);
                }
                TS3Infos.removeOnlineClient(leave_event.getClientId());
            }

            @Override
            public void onServerEdit(ServerEditedEvent server_edited_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_SE_EDIT)) {
                    listener.onServerEdit(server_edited_event);
                }
            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channel_edited_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_EDIT)) {
                    listener.onChannelEdit(channel_edited_event);
                }
            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channel_description_edited_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_DESCRIPTION)) {
                    listener.onChannelDescriptionChanged(channel_description_edited_event);
                }
            }

            @Override
            public void onClientMoved(ClientMovedEvent moved_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CL_MOVED)) {
                    listener.onClientMoved(moved_event);
                }
            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channel_create_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_CREATE)) {
                    listener.onChannelCreate(channel_create_event);
                }
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channel_deleted_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_DELETE)) {
                    listener.onChannelDeleted(channel_deleted_event);
                }
            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channel_moved_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_MOVED)) {
                    listener.onChannelMoved(channel_moved_event);
                }
            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channel_password_changed_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_CH_PASSWORD)) {
                    listener.onChannelPasswordChanged(channel_password_changed_event);
                }
            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilege_key_used_event) {
                for (TS3EventAdapter listener : event_listeners_.get(Types.EVENT_PRIV_KEY)) {
                    listener.onPrivilegeKeyUsed(privilege_key_used_event);
                }
            }
        };
    }
}
