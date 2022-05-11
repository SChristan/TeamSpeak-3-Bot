package com.features.twitchdisplay;

import com.Types;

public class StreamerInfo {

	private String user_login_;
	private String display_name_;
	private Types live_status_;

	public StreamerInfo(String user_login, String display_name, Types live_status) {
		this.display_name_ = display_name;
		this.user_login_ = user_login;
		this.live_status_ = live_status;
	}

	public String getDisplayName() {
		return display_name_;
	}

	public String getUserLogin() {
		return user_login_;
	}

	public void setUserLogin(String user_login) {
		this.user_login_ = user_login;
	}

	public Types getLiveStatus() {
		return live_status_;
	}

	public void setLiveStatus(Types live_status) {
		this.live_status_ = live_status;
	}

	public String getURL() {
		return "https://www.twitch.tv/" + user_login_;
	}
}
