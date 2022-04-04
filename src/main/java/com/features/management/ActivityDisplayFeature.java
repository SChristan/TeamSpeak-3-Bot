package com.features.management;

import java.sql.Statement;

import com.MySQL;
import com.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityDisplayFeature {

    private static final Logger log_ = LoggerFactory.getLogger(ActivityDisplayFeature.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL_management", "databaseUsername_management", "databasePassword_management");

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
