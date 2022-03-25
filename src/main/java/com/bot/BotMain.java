package com.bot;

import java.sql.Statement;

import com.mysql.MySQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotMain {
    
    private static final Logger log_ = LoggerFactory.getLogger(BotMain.class);
    private static MySQL sql_ = new MySQL(log_, "databaseURL", "databaseUsername", "databasePassword");

    public static void main(String[] args) {
        TS3Connection.connect();
    }

    public static Logger getLogger() {
        return log_;
    }

    public static Statement getSQLStatement() {
        return sql_.getStatement();
    }
}
