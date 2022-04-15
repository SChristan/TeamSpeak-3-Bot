package com.features.activitydisplay;

import com.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityDisplay {

    private static final Logger log_ = LoggerFactory.getLogger(ActivityDisplay.class);

    public static void start() {
        Commands.loadAuthorizedGroups();
        Utility.loadGroups();
        Utility.updateManagerClients();
        Events.startListen();
        ChannelDescription.update(Types.IS_MANAGER_AND_SUPPORTER);
    }

    public static void stop() {
        Events.stopListen();
    }

    public static Logger getLogger() {
        return log_;
    }
}
