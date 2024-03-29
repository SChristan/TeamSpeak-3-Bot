package com.features.activitydisplay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.MySQL;
import com.Types;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import com.ts3.TS3Client;

public class Utility {
    
    private static HashMap<String, TS3Client> manager_clients_ = new HashMap<String, TS3Client>();
	private static ArrayList<String> manager_afk_ = new ArrayList<>();
    private static List<ServergroupInfos> manager_groups_ = new ArrayList<>();
    private static List<ServergroupInfos> supporter_groups_ = new ArrayList<>();

    public static HashMap<String, TS3Client> getManagerClients() {
        return manager_clients_;
    }

    public static ArrayList<String> getManagerAFK() {
        return manager_afk_;
    }

    public static List<ServergroupInfos> getManagerGroups() {
        return manager_groups_;
    }

    public static List<ServergroupInfos> getSupporterGroups() {
        return supporter_groups_;
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
        for (ServergroupInfos group : Utility.getManagerGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    private static Boolean isSupporter(Client client) {
        for (ServergroupInfos group : Utility.getSupporterGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    public static void updateManagerClients() {
        manager_clients_.clear();
        for (ServergroupInfos servergroup : Utility.getManagerGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        for (ServergroupInfos servergroup : Utility.getSupporterGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        ActivityDisplay.getLogger().info("The manager list has been updated.");
    }

    public static void loadGroups() {
        try {
            ResultSet result;

            // Manager
            result = MySQL.getStatement().executeQuery("SELECT * FROM ad__channeldescription WHERE servergroup_designation='manager' ORDER BY sort ASC");
            manager_groups_.clear();
            while (result.next()) {
                manager_groups_.add(new ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }

            // Supporter
            result = MySQL.getStatement().executeQuery("SELECT * FROM ad__channeldescription WHERE servergroup_designation='supporter' ORDER BY sort ASC");
            supporter_groups_.clear();
            while (result.next()) {
                supporter_groups_.add(new ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }

            result.close();
            ActivityDisplay.getLogger().info("Groups were initialised.");
        } catch (SQLException e) {
            ActivityDisplay.getLogger().error("Database query failed.", e);
        }
    }
}
