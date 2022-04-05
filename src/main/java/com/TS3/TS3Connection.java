package com.TS3;

import java.util.Collections;

import com.bot.BotMain;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
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
		config_.setHost(TS3IDs.HOSTNAME);
		config_.setFloodRate(FloodRate.UNLIMITED);
		config_.setEnableCommunicationsLogging(TS3IDs.ENABLE_COMMUNICATIONS_LOGGING);
		config_.setReconnectStrategy(ReconnectStrategy.constantBackoff(200000));
		config_.setConnectionHandler(new ConnectionHandler() {
			@Override
			public void onConnect(TS3Api api) {
				api.login(TS3IDs.QUERY_LOGIN, TS3IDs.QUERY_PASSWORD);
				api.selectVirtualServerByPort(TS3IDs.PORT, TS3IDs.BOT_NICKNAME);
				api.registerEvents(TS3EventType.TEXT_PRIVATE, TS3EventType.SERVER, TS3EventType.CHANNEL);
				TS3Infos.loadOnlineClients(api);
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

        api_.editClient(api_.whoAmI().getId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, TS3IDs.VERSION));
	}
}
