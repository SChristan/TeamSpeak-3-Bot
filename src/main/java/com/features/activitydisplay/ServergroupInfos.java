package com.features.activitydisplay;

import java.util.ArrayList;
import java.util.List;

import com.TS3.TS3Connection;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;

public class ServergroupInfos {

    private int id_;
    private String title_;
    private List<ServerGroupClient> sg_clients_ = new ArrayList<>();
    private boolean clients_initialized = false;

    public ServergroupInfos(int servergroup_id, String title) {
        this.id_ = servergroup_id;
        this.title_ = title;
    }

    public int getID() {
        return id_;
    }

    public String getTitle() {
        return title_;
    }

    public List<ServerGroupClient> getSGClients() {
        if (!clients_initialized) {
            updateClients();
        }
        return sg_clients_;
    }

    public void updateClients() {
        sg_clients_.clear();
        for (ServerGroupClient sg_client : TS3Connection.getApi().getServerGroupClients(id_)) {
            sg_clients_.add(sg_client);
        }
        clients_initialized = true;
    }
}
