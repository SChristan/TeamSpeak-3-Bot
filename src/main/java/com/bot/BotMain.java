package com.bot;

import java.sql.Statement;

import com.TS3.TS3Connection;
import com.TS3.TS3Constants;
import com.TS3.TS3Events;
import com.features.management.ManagementBot;
import com.mysql.MySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotMain {
    
    private static final Logger log_ = LoggerFactory.getLogger(BotMain.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL", "databaseUsername", "databasePassword");

    public static void main(String[] args) {
        sql_.connect();
        TS3Constants.initialize();
        TS3Connection.connect();
        TS3Events.listen();
        ManagementBot.start();
    }

    public static Logger getLogger() {
        return log_;
    }

    public static Statement getSQLStatement() {
        return sql_.getStatement();
    }
}
