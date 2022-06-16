package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.bot.BotMain;

public class MySQL {

    private static String db_url_;
    private static String db_username_;
    private static String db_password_;

    private static Connection connection_;
    private static Statement statement_;
    
    public static void connect() {
        loadDBParameter();
        openConnection();
    }

    public static void disconnect() {
        try {
            statement_.close();
            connection_.close();
            BotMain.getLogger().info("Database connection was closed.");
        } catch (SQLException e) {
            BotMain.getLogger().error("Database disconnection error occured.", e);
        }
    }

    public static Statement getStatement() {
        try {
            if (connection_.isClosed()) {
                openConnection();
            }
        } catch (SQLException e) {
            BotMain.getLogger().error("Database access error occured.", e);
        }
        return statement_;
    }
    
    private static void openConnection() {
        try {
            connection_ = DriverManager.getConnection(db_url_, db_username_, db_password_);
            statement_ = connection_.createStatement();
            BotMain.getLogger().info("Database connection was established.");
        } catch (SQLException e) {
            BotMain.getLogger().error("Database access error occured.", e);
        }
    }

    private static void loadDBParameter() {
        try {
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

                if (parameter.equalsIgnoreCase("databaseURL"))
                    db_url_ = value;
                else if (parameter.equalsIgnoreCase("databaseUsername"))
                    db_username_ = value;
                else if (parameter.equalsIgnoreCase("databasePassword"))
                    db_password_ = value;
            }
            reader.close();
            filereader.close();
            BotMain.getLogger().info("Database parameter were loaded from config file.");
        } catch (IOException e) {
            BotMain.getLogger().error("File read failed.", e);
        }
    }
}
