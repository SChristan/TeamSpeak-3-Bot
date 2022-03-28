package com.TS3;

import com.features.management.Types;

public class TS3Client {

    private String nickname_;
    private String unique_identifier_;
    private Types activity_status_;

    public TS3Client(String nickname, String unique_identifier) {
        this.nickname_ = nickname;
        this.unique_identifier_ = unique_identifier;
        updateActivityStatus();
    }
    
    public String getURL() {
        return "[URL=client://0/" + getUniqueIdentifier() + "~" + getNickname().replace(" ", "%20").replace("[", "%5B").replace("]", "%5D") + "]" + getNickname() + "[/URL]";
    }

    public String getNickname() {
        return nickname_;
    }

    public String getUniqueIdentifier() {
        return unique_identifier_;
    }

    public Types getActivityStatus() {
        return activity_status_;
    }

    public void setActivityStatus(Types activity_status) {
        this.activity_status_ = activity_status;
    }

    public void updateActivityStatus() {
        if (TS3Connection.getApi().isClientOnline(unique_identifier_)) {
            int channel_id = TS3Connection.getApi().getClientByUId(unique_identifier_).getChannelId();
            if (channel_id == TS3IDs.CHANNEL_ID_AFK_SHORT || channel_id == TS3IDs.CHANNEL_ID_AFK_LONG) {
				activity_status_ = Types.IS_AFK;
			} else {
                activity_status_ = Types.IS_ONLINE;
            }
        } else {
            activity_status_ = Types.IS_OFFLINE;
        }
    }
}
