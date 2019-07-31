package com.craftsteamg.helix.config;

public class BotConfig {

	private String botToken;
	private String dbUsername;
	private String dbPassword;
	private String dbIP;
	private String dbName;
	private String prefix;

	public BotConfig() {} //For Serialization

	public String getBotToken() {
		return botToken;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbIP() {
		return dbIP;
	}

	public String getDbName() {
		return dbName;
	}

	public String getPrefix() {
		return prefix;
	}
}
