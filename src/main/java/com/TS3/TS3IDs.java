package com.TS3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.bot.BotMain;

public class TS3IDs {
    // Code version
    public static String VERSION = "Version 1.0.0";

    // Config
    public static String HOSTNAME;
	public static int PORT;
	public static String QUERY_LOGIN;
	public static String QUERY_PASSWORD;
	public static String BOT_NICKNAME;
	public static boolean ENABLE_COMMUNICATIONS_LOGGING;

    // Channel IDs
	public static int CHANNEL_ID_MC;            // "Management Mitglieder"
	public static int CHANNEL_ID_SC;            // "Supportansprechpartner"
	public static int CHANNEL_ID_WABE;          // "Warten auf Bewerbungsgespräch"
	public static int CHANNEL_ID_WAMM;          // "Warten auf Management"
	public static int CHANNEL_ID_AFK_SHORT;     // "Kurz AFK"
	public static int CHANNEL_ID_AFK_LONG;      // "Lang AFK"

    public static void initialize() {
        try {
            ResultSet result;

            // Bot config
            HashMap<String, String> config_map_ = new HashMap<String, String>();
            result = BotMain.getSQLStatement().executeQuery("SELECT * FROM bot_config");
            while (result.next()) {
                config_map_.put(result.getString("config_designation"), result.getString("value"));
            }

            HOSTNAME = config_map_.get("ip_address");
            PORT = Integer.parseInt(config_map_.get("server_port"));
            QUERY_LOGIN = config_map_.get("query_username");
            QUERY_PASSWORD = config_map_.get("query_password");
            BOT_NICKNAME = config_map_.get("bot_nickname");
            ENABLE_COMMUNICATIONS_LOGGING = Boolean.parseBoolean(config_map_.get("enable_communications_logging"));
            
            // Channel IDs
            HashMap<String, Integer> channelID_map = new HashMap<String, Integer>();
            result = BotMain.getSQLStatement().executeQuery("SELECT * FROM channel_ids");
            while (result.next()) {
                channelID_map.put(result.getString("channel_designation"), result.getInt("channel_id"));
            }

            CHANNEL_ID_MC = channelID_map.get("management_mitglieder");
            CHANNEL_ID_SC = channelID_map.get("support_mitglieder");
            CHANNEL_ID_WABE = channelID_map.get("warten_bewerbungsgespraech");
            CHANNEL_ID_WAMM = channelID_map.get("warten_management");
            CHANNEL_ID_AFK_SHORT = channelID_map.get("afk_short");
            CHANNEL_ID_AFK_LONG = channelID_map.get("afk_long");

            result.close();
            BotMain.getLogger().info("TS3Constants were initialised.");
        } catch (NumberFormatException | SQLException e) {
            BotMain.getLogger().error("Exception in TS3Constants initialize():", e);
        }
	}
}
