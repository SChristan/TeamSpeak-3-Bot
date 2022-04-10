package com.features.maxServerClients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaxServerClients {
    
    private static final Logger log_ = LoggerFactory.getLogger(MaxServerClients.class);

    public static void start() {
        Events.startListen();
    }

    public static void stop() {
        Events.stopListen();
    }

    public static Logger getLogger() {
        return log_;
    }
}
