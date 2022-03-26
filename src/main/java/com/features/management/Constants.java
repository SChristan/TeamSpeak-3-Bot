package com.features.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    private static List<Integer> GROUPS_AUTHORIZED = new ArrayList<>();
    private static List<TS3ServergroupInfos> GROUPS_MANAGER = new ArrayList<>();
	private static List<TS3ServergroupInfos> GROUPS_SUPPORTER = new ArrayList<>();

    public static List<Integer> getAuthorizedGroups() {
        return GROUPS_AUTHORIZED;
    }

    public static List<TS3ServergroupInfos> getManagerGroups() {
        return GROUPS_MANAGER;
    }

    public static List<TS3ServergroupInfos> getSupporterGroups() {
        return GROUPS_SUPPORTER;
    }

    public static void initialize() {
        try {
            ResultSet result;

            // Authorized groups
            result = ManagementBot.getSQLStatement().executeQuery("SELECT * FROM verified_servergroups");
            while (result.next()) {
                GROUPS_AUTHORIZED.add(result.getInt("group_id"));
            }
            
            // Manager
            result = ManagementBot.getSQLStatement().executeQuery("SELECT * FROM channeldescription_content WHERE servergroup_designation='manager' ORDER BY sort ASC");
            while (result.next()) {
                GROUPS_MANAGER.add(new TS3ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }
            
            // Supporter
            result = ManagementBot.getSQLStatement().executeQuery("SELECT * FROM channeldescription_content WHERE servergroup_designation='supporter' ORDER BY sort ASC");
            while (result.next()) {
                GROUPS_SUPPORTER.add(new TS3ServergroupInfos(result.getInt("servergroup_id"), result.getString("servergroup_header") + "\n"));
            }

            result.close();
            ManagementBot.getLogger().info("Constants were initialised.");
        } catch (SQLException e) {
            ManagementBot.getLogger().error("Exception in Constants initialize():", e);
        }
	}
}
