package com.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;

public class MySQL {

    private String db_url_;
    private String db_username_;
    private String db_password_;

    private String db_url_config_name_;
    private String db_username_config_name_;
    private String db_password_config_name_;

    private Connection connection_;
    private Statement statement_;

    private Logger log_;

    public MySQL(Logger log, String db_url_config_name, String db_username_config_name, String db_password_config_name) {
        log_ = log;
        db_url_config_name_ = db_url_config_name;
        db_username_config_name_ = db_username_config_name;
        db_password_config_name_ = db_password_config_name;
    }
    
    public void connect() {
        try {
            loadDBParameter();
            connection_ = DriverManager.getConnection(db_url_, db_username_, db_password_);
            statement_ = connection_.createStatement();
            log_.info("Database connection was established.");
        } catch (IOException | SQLException e) {
            log_.error("Exception in MySQL connect():", e);
        }
    }

    public void disconnect() {
        try {
            statement_.close();
            connection_.close();
            log_.info("Database connection was closed.");
        } catch (SQLException e) {
            log_.error("Exception in MySQL disconnect():", e);
        }
    }

    public Statement getStatement() {
        return statement_;
    }

    private void loadDBParameter() throws IOException {
        String line;
        File file = new File("BotConfig.txt");
        FileReader filereader = new FileReader(file);
        BufferedReader reader = new BufferedReader(filereader);

        while ((line = reader.readLine()) != null) {
            if (line.indexOf('=') == -1)
                continue;

            int index = line.indexOf('=');
            String parameter = line.substring(0, index);
            String value = line.substring(index + 1);

            if (parameter.equalsIgnoreCase(db_url_config_name_))
                db_url_ = value;
            else if (parameter.equalsIgnoreCase(db_username_config_name_))
                db_username_ = value;
            else if (parameter.equalsIgnoreCase(db_password_config_name_))
                db_password_ = value;
        }
        reader.close();
        filereader.close();
        log_.info("Database parameter were loaded from config file.");
    }
}
