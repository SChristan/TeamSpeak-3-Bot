package com.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotMain {
    
    private static final Logger log_ = LoggerFactory.getLogger(BotMain.class);

    public static void main(String[] args) {

        TS3Connection.connect();
    }

    public static Logger getLogger() {
        return log_;
    }
}
