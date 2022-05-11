package com.features.twitchdisplay;

import com.Types;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.ts3.TS3Events;

public class Events {

    private static TS3EventAdapter event_adapter_;

    public static void startListen() {
        createEventAdapter();
        TS3Events.addListener(event_adapter_, Types.EVENT_TEXT);
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
        };
    }
}
