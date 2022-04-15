package com.bot;

import com.MySQL;
import com.features.activitydisplay.ActivityDisplay;
import com.features.maxserverclients.MaxServerClients;
import com.features.welcomemessage.WelcomeMessage;
import com.ts3.TS3Connection;
import com.ts3.TS3Events;
import com.ts3.TS3IDs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotMain {

    private static final Logger log_ = LoggerFactory.getLogger(BotMain.class);

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        MySQL.connect();
        TS3IDs.load();
        Commands.loadAuthorizedGroups();
        TS3Connection.connect();
        TS3Events.startListen();
        ActivityDisplay.start();
        MaxServerClients.start();
        WelcomeMessage.start();
        log_.info("The bot was started.");
    }

    public static void stop() {
        ActivityDisplay.stop();
        MaxServerClients.stop();
        WelcomeMessage.stop();
        TS3Events.stopListen();
        TS3Connection.disconnect();
        MySQL.disconnect();
        log_.info("The bot was stopped.");
    }

    public static void exit() {
        stop();
        log_.info("The system will be terminated.");
        System.exit(0);
    }

    public static Logger getLogger() {
        return log_;
    }
}
