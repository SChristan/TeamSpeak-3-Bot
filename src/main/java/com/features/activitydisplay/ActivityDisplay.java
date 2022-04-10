package com.features.activitydisplay;

import java.sql.Statement;

import com.MySQL;
import com.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityDisplay {

    private static final Logger log_ = LoggerFactory.getLogger(ActivityDisplay.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL_activitydisplay", "databaseUsername_activitydisplay", "databasePassword_activitydisplay");

    public static void start() {
        sql_.connect();
        Commands.loadAuthorizedGroups();
        Utility.loadGroups();
        Utility.updateManagerClients();
        Events.startListen();
        ChannelDescription.update(Types.IS_MANAGER_AND_SUPPORTER);
    }

    public static void stop() {
        Events.stopListen();
        sql_.disconnect();
    }

    public static Logger getLogger() {
        return log_;
    }

    public static Statement getSQLStatement() {
        return sql_.getStatement();
    }
}
