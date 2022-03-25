package com.bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;

public class TS3Events {

    private static TS3Api api_ = TS3Connection.getApi();

    public static void listen() {
        api_.addTS3Listeners(new TS3EventAdapter() {

            @Override
            public void onTextMessage(TextMessageEvent textEvent) {}

            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {}

            @Override
            public void onClientLeave(ClientLeaveEvent leaveEvent) {}

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {}

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {}

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {}

            @Override
            public void onClientMoved(ClientMovedEvent movedEvent) {}

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {}

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {}

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {}

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {}

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {}
        });
    }
}
