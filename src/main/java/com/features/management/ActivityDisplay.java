package com.features.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.TS3.TS3Client;
import com.TS3.TS3Connection;
import com.TS3.TS3Constants;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

public class ActivityDisplay {
    
    private static HashMap<String, TS3Client> manager_clients_ = new HashMap<String, TS3Client>();
	public static ArrayList<String> manager_afk_ = new ArrayList<>();

    public static HashMap<String, TS3Client> getManagerClients() {
        return manager_clients_;
    }

    public static ArrayList<String> getManagerAFK() {
        return manager_afk_;
    }

	public static Types getClientRole(Client client) {
		if (isManager(client) && isSupporter(client)) {
            return Types.IS_MANAGER_AND_SUPPORTER;
        } else if (isManager(client)) {
            return Types.IS_MANAGER;
        } else if (isSupporter(client)) {
            return Types.IS_SUPPORTER;
        } else {
            return Types.IS_MEMBER;
        }
	}

    private static Boolean isManager(Client client) {
        for (TS3ServergroupInfos group : Constants.getManagerGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    private static Boolean isSupporter(Client client) {
        for (TS3ServergroupInfos group : Constants.getSupporterGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    public static void updateManagerClients() {
        manager_clients_.clear();
        for (TS3ServergroupInfos servergroup : Constants.getManagerGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        for (TS3ServergroupInfos servergroup : Constants.getSupporterGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        ManagementBot.getLogger().info("The manager list has been updated.");
    }

    public static String getDescription(List<TS3ServergroupInfos> group) {
        String description = "";
        for (TS3ServergroupInfos servergroup : group) {
            String online_url = "";
            String afk_url = "";
            String offline_url = "";

            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = manager_clients_.get(client.getUniqueIdentifier());
                Types activity_status = ts3_client.getActivityStatus();
                if (activity_status == Types.IS_ONLINE) {
                    online_url = online_url + "→ [COLOR=GREEN][B][ON][/B][/COLOR] " + ts3_client.getURL() + "\n";
                } else if (activity_status == Types.IS_AFK) {
                    afk_url = afk_url + "→ [COLOR=#FFD100][B][AFK][/B][/COLOR] " + ts3_client.getURL() + "\n";
                } else if (activity_status == Types.IS_OFFLINE) {
                    offline_url = offline_url + "→ [COLOR=RED][B][OFF][/B][/COLOR] " + ts3_client.getURL() + "\n";
                }
            }
            if (servergroup.getSGClients().size() == 0)
            online_url = "[B]    [COLOR=BLACK]-TBD-[/COLOR][/B]\n";

            description = description + servergroup.getTitle() + online_url + afk_url + offline_url + "\n";
        }
        return description;
    }

    public static void updateChannelDescription(Types type) {
        if (type == Types.IS_MANAGER_AND_SUPPORTER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_MC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(Constants.getManagerGroups()));
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_SC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(Constants.getSupporterGroups()));
		} else if (type == Types.IS_MANAGER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_MC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(Constants.getManagerGroups()));
		} else if (type == Types.IS_SUPPORTER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_SC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(Constants.getSupporterGroups()));
		}
    }
}
