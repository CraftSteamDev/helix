package com.craftsteamg.helix.internal.listeners;

import com.craftsteamg.helix.api.DiscordMsg;
import com.craftsteamg.helix.api.profiles.UserProfile;
import com.craftsteamg.helix.database.DatabaseHelper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import javax.annotation.Nonnull;

public class MessageListeners extends ListenerAdapter {


	private MongoDatabase database;
	private MongoCollection<Document> messageLogs;
	private MongoCollection<Document> profiles;
	private final String prefix;

	public MessageListeners(MongoDatabase database, String prefix) {
		this.database = database;
		this.messageLogs = database.getCollection("messageLogs");
		this.profiles = database.getCollection("profiles");
		this.prefix = prefix;
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

		if(event.getAuthor().isBot())
			return;

		DiscordMsg discordMsg = new DiscordMsg(event.getMessage());
		DatabaseHelper.storeMessage(messageLogs, discordMsg);

		User user = event.getAuthor();
		UserProfile profile = DatabaseHelper.getOrCreateProfile(profiles, user.getIdLong());

		if(profile.isMuted()) {
			event.getMessage().delete().queue();
			return;
		}

		if(!event.getMessage().getContentRaw().trim().startsWith(prefix))
			return;

	}

	@Override
	public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
		if(event.getAuthor().isBot())
			return;

		DiscordMsg discordMsg = new DiscordMsg(event.getMessage());
		User user = event.getAuthor();
		UserProfile profile = DatabaseHelper.getOrCreateProfile(profiles, user.getIdLong());

		if(profile.isMuted()) {
			event.getMessage().delete().queue();
			return;
		}

		DatabaseHelper.updateMessage(messageLogs, discordMsg);
	}
}
