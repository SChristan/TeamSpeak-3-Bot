package com.bot;

import java.util.HashMap;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class TS3Infos {
    
    private static HashMap<Integer, Client> online_clients_ = new HashMap<Integer, Client>();

    public static HashMap<Integer, Client> getOnlineClients() {
        return online_clients_;
    }

    public static void addOnlineClient(Client client) {
        online_clients_.put(client.getId(), client);
    }

    public static void removeOnlineClient(int client_id) {
        online_clients_.remove(client_id);
    }
    
    public static void loadOnlineClients(TS3Api api) {
        online_clients_.clear();
        for (Client client : api.getClients()) {
            online_clients_.put(client.getId(), client);
        }
        BotMain.getLogger().info("All online clients have been loaded!");
    }
}
