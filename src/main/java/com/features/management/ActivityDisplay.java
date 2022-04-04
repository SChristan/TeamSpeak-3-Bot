package com.features.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.Types;
import com.TS3.TS3Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

public class ActivityDisplay {
    
    private static HashMap<String, TS3Client> manager_clients_ = new HashMap<String, TS3Client>();
	private static ArrayList<String> manager_afk_ = new ArrayList<>();
    private static List<TS3ServergroupInfos> manager_groups_ = new ArrayList<>();
    private static List<TS3ServergroupInfos> supporter_groups_ = new ArrayList<>();

    public static HashMap<String, TS3Client> getManagerClients() {
        return manager_clients_;
    }

    public static ArrayList<String> getManagerAFK() {
        return manager_afk_;
    }

    public static List<TS3ServergroupInfos> getManagerGroups() {
        return manager_groups_;
    }

    public static List<TS3ServergroupInfos> getSupporterGroups() {
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
        for (TS3ServergroupInfos group : ActivityDisplay.getManagerGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    private static Boolean isSupporter(Client client) {
        for (TS3ServergroupInfos group : ActivityDisplay.getSupporterGroups()) {
            if (client.isInServerGroup(group.getID()))
                return true;
        }
        return false;
    }

    public static void updateManagerClients() {
        manager_clients_.clear();
        for (TS3ServergroupInfos servergroup : ActivityDisplay.getManagerGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        for (TS3ServergroupInfos servergroup : ActivityDisplay.getSupporterGroups()) {
            servergroup.updateClients();
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = new TS3Client(client.getNickname(), client.getUniqueIdentifier());
                manager_clients_.put(client.getUniqueIdentifier(), ts3_client);
            }
        }
        ManagementBot.getLogger().info("The manager list has been updated.");
    }

    public static void loadGroups() {
        try {
            manager_groups_.clear();
            supporter_groups_.clear();

            ResultSet result;

            // Manager
            result = ManagementBot.getSQLStatement().executeQuery("SELECT * FROM channeldescription_content WHERE servergroup_designation='manager' ORDER BY sort ASC");
            while (result.next()) {
                manager_groups_.add(new TS3ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }

            // Supporter
            result = ManagementBot.getSQLStatement().executeQuery("SELECT * FROM channeldescription_content WHERE servergroup_designation='supporter' ORDER BY sort ASC");
            while (result.next()) {
                supporter_groups_.add(new TS3ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }

            result.close();
            ManagementBot.getLogger().info("Groups were initialised.");
        } catch (SQLException e) {
            ManagementBot.getLogger().error("Exception in Constants initialize():", e);
        }
    }
}
