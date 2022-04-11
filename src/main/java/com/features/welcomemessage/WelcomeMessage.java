package com.features.welcomemessage;

import java.sql.Statement;

import com.MySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeMessage {

    private static final Logger log_ = LoggerFactory.getLogger(WelcomeMessage.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL_welcomemessage", "databaseUsername_welcomemessage", "databasePassword_welcomemessage");

    public static void start() {
        sql_.connect();
    }

    public static void stop() {
        sql_.disconnect();
    }

    public static Logger getLogger() {
        return log_;
    }

    public static Statement getSQLStatement() {
        return sql_.getStatement();
    }
}
