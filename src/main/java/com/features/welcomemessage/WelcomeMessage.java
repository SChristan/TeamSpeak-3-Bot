package com.features.welcomemessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeMessage {

    private static final Logger log_ = LoggerFactory.getLogger(WelcomeMessage.class);

    public static void start() {
        Commands.loadAuthorizedGroups();
        Utility.loadMessage();
        Events.startListen();
    }

    public static void stop() {
        Events.stopListen();
    }

    public static Logger getLogger() {
        return log_;
    }
}
