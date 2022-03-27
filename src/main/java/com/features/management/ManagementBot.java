package com.features.management;

import java.sql.Statement;

import com.mysql.MySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementBot {

    private static final Logger log_ = LoggerFactory.getLogger(ManagementBot.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL", "databaseUsername", "databasePassword");

    public static void start() {
        sql_.connect();
        ActivityDisplay.loadGroups();
        ActivityDisplay.updateManagerClients();
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
