package com.features.twitchdisplay;

import java.util.Map.Entry;

import com.Types;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.ts3.TS3Connection;
import com.ts3.TS3IDs;

public class ChannelDescription {

    private static String channel_description_old = "";

    public static void update() {

        String gm_stream = "";
        String streamers_live = "";
        String streamers_offline = "";
        String log_info = "";

        for (Entry<String, StreamerInfo> streamer : Utility.getStreamer().entrySet()) {
            if (streamer.getValue().getLiveStatus() == Types.TWITCH_LIVE) {
                if (streamer.getValue().getUserLogin().equals("germanmonkeys")) {
                    gm_stream = "→ [B][COLOR=GREEN][LIVE][/COLOR] GermanMonkeys[/B] [URL=" + streamer.getValue().getURL() + "]Zum Stream[/URL]\n";
                    log_info = "GermanMonkeys, ";
                } else {
                    streamers_live = streamers_live + "→ [B][COLOR=GREEN][LIVE][/COLOR] " + streamer.getValue().getDisplayName() + "[/B] " + "[URL=" + streamer.getValue().getURL() + "]Zum Stream[/URL]\n";
                    log_info = log_info + streamer.getValue().getDisplayName() + ", ";
                }
            } else {
                if (streamer.getValue().getUserLogin().equals("germanmonkeys")) {
                    gm_stream = "→ [B][COLOR=RED][OFFLINE][/COLOR] GermanMonkeys[/B] [URL=" + streamer.getValue().getURL() + "]Zum Stream[/URL]\n";
                } else {
                    streamers_offline = streamers_offline + "→ [B][COLOR=RED][OFFLINE][/COLOR] " + streamer.getValue().getDisplayName() + "[/B] " + "[URL=" + streamer.getValue().getURL() + "]Zum Stream[/URL]\n";
                }
            }
        }

        String channel_description = "[B][color=royalblue]Willkommen im Streaming Channel.[/COLOR]\n\n" + "Besuche doch unseren offiziellen Twitchkanal:\n" + gm_stream + "_________________________\n\n" + "[color=darkorange]Unsere Community Streamer:[/COLOR][/B]\n\n" + streamers_live + streamers_offline + "\n[B][COLOR=purple]Wenn du selbst Streamer bei den GermanMonkeys werden möchtest, dann schau einfach bei unseren [URL=https://germanmonkeys.de/streamer-voraussetzungen/]Streamer Voraussetzungen[/URL] vorbei![/COLOR][/B]";

        if (!channel_description.equals(channel_description_old)) {
            TS3Connection.getApi().editChannel(TS3IDs.CHANNEL_ID_STREAMER, ChannelProperty.CHANNEL_DESCRIPTION, channel_description);
            channel_description_old = channel_description;
            if (log_info == "")
                TwitchDisplay.getLogger().info("There is no streamer live anymore.");
            else
                TwitchDisplay.getLogger().info("Streamer currently live: " + log_info);
        }
    }
}
