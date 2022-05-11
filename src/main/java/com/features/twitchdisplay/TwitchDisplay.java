package com.features.twitchdisplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitchDisplay {
    
    private static final Logger log_ = LoggerFactory.getLogger(TwitchDisplay.class);

    public static void start() {
        Commands.loadAuthorizedGroups();
        Authentication.loadClientCredentials();
        Utility.loadStreamer();
        TwitchListener.listen();
        Events.startListen();
    }

    public static void stop() {
        Events.stopListen();
    }

    public static Logger getLogger() {
        return log_;
    }
}
