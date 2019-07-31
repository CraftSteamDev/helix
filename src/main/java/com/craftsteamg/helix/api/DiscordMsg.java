package com.craftsteamg.helix.api;

import com.google.gson.Gson;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class DiscordMsg {

	private static final Gson GSON = new Gson();

	/**
	 * Non-Persistent Data
	 */
	private transient Message message;
	private transient User author;
	@Nullable
	private transient Guild guild;
	private transient MessageChannel channel;

	/**
	 * Persistent Data
	 */
	private long messageID;
	private long channelID;
	private String messageContent;
	private String authorName;
	private short authorDiscrim;
	private long epochCreated;
	private Set<String> previousContent = new HashSet<>();

	public DiscordMsg() {} //For Serialization
	public DiscordMsg(Message message) {
		this.message = message;
		if(this.message.isFromGuild())
			this.guild = message.getGuild();
		this.channel = message.getChannel();
		this.channelID = this.channel.getIdLong();
		this.author = message.getAuthor();
		this.authorName = this.author.getName();
		this.messageID = message.getIdLong();
		this.messageContent = message.getContentRaw();
		this.authorDiscrim = Short.parseShort(message.getAuthor().getDiscriminator().substring(1));
		this.epochCreated = this.message.getTimeCreated().toEpochSecond();
	}

	public DiscordMsg(Document document) {

	}

	@Override
	public String toString() {
		return this.authorName + "#" + this.getAuthorDiscrim() + ": " + this.messageContent;
	}

	public String getAuthorDiscrim() {
		int toPlace = 4 - String.valueOf(authorDiscrim).length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < toPlace; i++) {
			builder.append("0");
		}
		builder.append(authorDiscrim);
		return builder.toString();
	}

	public Set<String> getPreviousContent() {
		return previousContent;
	}

	public void reply(String message) {
		this.channel.sendMessage(message).queue();
	}


	public String toJson() {
		return GSON.toJson(this, getClass());
	}

	public static DiscordMsg fromJson(String json) {
		if(json == null || json.isEmpty())
			return null;
		return GSON.fromJson(json, DiscordMsg.class);
	}

	public Document toDocument() {
		return Document.parse(toJson());
	}

	public static DiscordMsg fromDocument(Document document) {
		JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
		return fromJson(document.toJson(jsonWriterSettings));
	}

	public boolean setup(JDA jda) {
		try {
			TextChannel channel = jda.getTextChannelById(this.channelID);
			Message message = channel.retrieveMessageById(this.messageID).complete();
			this.message = message;
			if(this.message.isFromGuild())
				this.guild = message.getGuild();
			this.channel = message.getChannel();
			this.channelID = this.channel.getIdLong();
			this.author = message.getAuthor();
			this.authorName = this.author.getName();
			this.messageID = message.getIdLong();
			this.messageContent = message.getContentRaw();
			this.authorDiscrim = Short.parseShort(message.getAuthor().getDiscriminator().substring(1));
			this.epochCreated = this.message.getTimeCreated().toEpochSecond();
			return true;
		}catch (Exception e) {
			return false;
		}
	}


}

