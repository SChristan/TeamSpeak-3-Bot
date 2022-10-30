package com.features.twitchdisplay;

import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.Types;

import org.json.JSONArray;
import org.json.JSONObject;

public class TwitchListener {

    private static Timer timer_;

    public static void stopListen() {
        timer_.cancel();
    }

    public static void listen() {
        timer_ = new Timer();
        timer_.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    String user_logins = "";
                    for (Entry<String, StreamerInfo> streamer : Utility.getStreamer().entrySet()) {
                        user_logins = user_logins + "user_login=" + streamer.getValue().getUserLogin() + "&";
                    }

                    for (Entry<String, StreamerInfo> streamer : Utility.getStreamer().entrySet()) {
                        streamer.getValue().setLiveStatus(Types.TWITCH_OFFLINE);
                    }

                    JSONObject response_root = Utility.twitchAPIRequest("https://api.twitch.tv/helix/streams?" + user_logins);
                    JSONArray response_data = response_root.getJSONArray("data");

                    for (int i = 0; i < response_data.length(); i++) {
                        Utility.getStreamer().get(response_data.getJSONObject(i).getString("user_login").toLowerCase()).setLiveStatus(Types.TWITCH_LIVE);
                    }
                    ChannelDescription.update();
                } catch (Exception e) {
                    TwitchDisplay.getLogger().error("Twitch Listener threw an exception.", e);
                }
            }

        }, 0, 10 * 1000);
    }
}
