package com.features.maxserverclients;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServerInfo;
import com.ts3.TS3Connection;
import com.ts3.TS3Events;
import com.ts3.TS3Infos;

public class Events {

    private static TS3EventAdapter event_adapter_;
    private static int max_clients_ = 200;

    public static void startListen() {
        createEventAdapter();
        TS3Events.addListener(event_adapter_);
    }

    public static void stopListen() {
        TS3Events.removeListener(event_adapter_);
    }

    private static void createEventAdapter() {
        event_adapter_ = new TS3EventAdapter() {
            
            @Override
            public void onClientJoin(ClientJoinEvent joinEvent) {
                if (TS3Infos.getOnlineClients().size() > 180) {
                    VirtualServerInfo server_info = TS3Connection.getApi().getServerInfo();
                    int clients_online = server_info.getClientsOnline();
                    int max_clients = server_info.getMaxClients();

                    if (max_clients - clients_online <= 5) {
                        max_clients_ = max_clients + 20;
                        Map<VirtualServerProperty, String> properties = new HashMap<>();
                        properties.put(VirtualServerProperty.VIRTUALSERVER_MAXCLIENTS, String.valueOf(max_clients_));
                        TS3Connection.getApi().editServer(properties);
                        MaxServerClients.getLogger().info("Maximum clients increased to " + (max_clients + 20) + ".");
                    } else if (max_clients > 200 && clients_online < max_clients - 40) {
                        max_clients_ = max_clients - 20;
                        Map<VirtualServerProperty, String> properties = new HashMap<>();
                        properties.put(VirtualServerProperty.VIRTUALSERVER_MAXCLIENTS, String.valueOf(max_clients_));
                        TS3Connection.getApi().editServer(properties);
                        MaxServerClients.getLogger().info("Maximum clients decreased to " + (max_clients - 20) + ".");
                    }
                }
            }

            @Override
            public void onClientLeave(ClientLeaveEvent e) {
                if (max_clients_ > 200 && TS3Infos.getOnlineClients().size() < 180) {
                    max_clients_ = 200;
                    Map<VirtualServerProperty, String> properties = new HashMap<>();
                    properties.put(VirtualServerProperty.VIRTUALSERVER_MAXCLIENTS, String.valueOf(max_clients_));
                    TS3Connection.getApi().editServer(properties);
                    MaxServerClients.getLogger().info("Maximum clients decreased to " + (max_clients_) + ".");
                }
            }
        };
    }
}
