package com.craftsteamg.helix.api.profiles;

import com.craftsteamg.helix.internal.objects.Warning;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {



	private long id;
	private String user;
	private short discrim;
	private ArrayList<Warning> warnings = new ArrayList<>();
	private boolean muted = false;


	public UserProfile() {

	}

	public UserProfile(long id, String user, short discrim) {
		this.id = id;
		this.user = user;
		this.discrim = discrim;
	}

	public UserProfile(long id) {
		this.id = id;
	}


	public List<Warning> getWarnings() {
		return warnings;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static UserProfile fromJson(String json) {
		return new Gson().fromJson(json, UserProfile.class);
	}

	public String toJson() {
		return new Gson().toJson(this, UserProfile.class);
	}

	public String getAuthorDiscrim() {
		int toPlace = 4 - String.valueOf(discrim).length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < toPlace; i++) {
			builder.append("0");
		}
		builder.append(discrim);
		return builder.toString();
	}

	public String getUser() {
		return user;
	}
}
