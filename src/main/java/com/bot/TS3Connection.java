package com.bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

public class TS3Connection {

	private static TS3Api api_;
	private static TS3Query query_;
	private static TS3Config config_;

	public static TS3Api getApi() {
		return api_;
	}

	public static TS3Query getQuery() {
		return query_;
	}

	public static void disconnect() {
		query_.exit();
		BotMain.getLogger().info("TS3Query has disconnected from the Teamspeak server.");
	}

	public static void connect() {
		config_ = new TS3Config();
		config_.setHost("hostname");
		config_.setFloodRate(FloodRate.UNLIMITED);
		config_.setEnableCommunicationsLogging(true);
		config_.setReconnectStrategy(ReconnectStrategy.constantBackoff(200000));
		config_.setConnectionHandler(new ConnectionHandler() {
			@Override
			public void onConnect(TS3Query ts3Query) {
				TS3Api api = ts3Query.getApi();
				api.login("username", "password");
				api.selectVirtualServerByPort(10050, "nickname");
				api.registerEvents(TS3EventType.TEXT_PRIVATE, TS3EventType.SERVER, TS3EventType.CHANNEL);
			}

			@Override
			public void onDisconnect(TS3Query ts3Query) {
				// Auto-generated method stub
			}
		});
		query_ = new TS3Query(config_);
		api_ = query_.getApi();
		query_.connect();
		BotMain.getLogger().info("TS3Query has connected to the Teamspeak server.");
	}
}
