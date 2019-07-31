package com.craftsteamg.helix.internal;

import com.craftsteamg.helix.config.BotConfig;
import com.craftsteamg.helix.internal.listeners.MessageListeners;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.JDA;

public class BotManager {


	private final JDA jda;
	private final MongoDatabase database;
	private final String prefix;

	public BotManager(JDA jda, MongoDatabase database, String prefix) {
		this.jda = jda;
		this.database = database;
		this.prefix = prefix;
	}

	public void registerListeners() {
		this.jda.addEventListener(new MessageListeners(database, prefix));
	}

}
