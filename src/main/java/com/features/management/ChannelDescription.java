package com.features.management;

import java.util.List;

import com.TS3.TS3Client;
import com.TS3.TS3Connection;
import com.TS3.TS3Constants;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

public class ChannelDescription {

    public static String getDescription(List<TS3ServergroupInfos> group) {
        String description = "";
        for (TS3ServergroupInfos servergroup : group) {
            String online_url = "";
            String afk_url = "";
            String offline_url = "";
    
            for (ServerGroupClient client : servergroup.getSGClients()) {
                TS3Client ts3_client = ActivityDisplay.getManagerClients().get(client.getUniqueIdentifier());
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

    public static void update(Types type) {
        if (type == Types.IS_MANAGER_AND_SUPPORTER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_MC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(ActivityDisplay.getManagerGroups()));
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_SC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(ActivityDisplay.getSupporterGroups()));
    	} else if (type == Types.IS_MANAGER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_MC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(ActivityDisplay.getManagerGroups()));
    	} else if (type == Types.IS_SUPPORTER) {
            TS3Connection.getApi().editChannel(TS3Constants.CHANNEL_ID_SC, ChannelProperty.CHANNEL_DESCRIPTION, getDescription(ActivityDisplay.getSupporterGroups()));
    	}
    }
}