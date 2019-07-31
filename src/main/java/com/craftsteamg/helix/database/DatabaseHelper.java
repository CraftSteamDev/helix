package com.craftsteamg.helix.database;

import com.craftsteamg.helix.api.DiscordMsg;
import com.craftsteamg.helix.api.profiles.UserProfile;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseHelper {

	public static void storeMessage(MongoCollection<Document> collection, DiscordMsg msg) {
		collection.insertOne(msg.toDocument());
	}

	public static void removeMessage(MongoCollection<Document> collection, long id) {
		Document doc = collection.find(eq("messageID", id))
				.first();

		if (doc == null || doc.isEmpty())
			return;

		collection.deleteOne(doc);
	}

	public static void updateMessage(MongoCollection<Document> collection, DiscordMsg msg) {
		Document document = collection.find(eq("messageID", msg.getMessageID())).first();
		if(document == null)
			return;

		DiscordMsg old = DiscordMsg.fromDocument(document);

		if(old.getMessageContent().equalsIgnoreCase(msg.getMessageContent()))
			return;

		msg.getPreviousContent().add(old.getMessageContent());
		old.getPreviousContent().forEach(e -> msg.getPreviousContent().add(e));

		collection.replaceOne(document, msg.toDocument());
	}

	public static Optional<DiscordMsg> lookupMessage(MongoCollection<Document> collection, long id) {
		Document document = collection.find(eq("messageID", id)).first();
		if (document == null)
			return Optional.empty();
		DiscordMsg msg = DiscordMsg.fromDocument(document);
		return Optional.ofNullable(msg);
	}

	public static UserProfile getOrCreateProfile(MongoCollection<Document> collection, long id) {
		Document document = collection.find(eq("id", id)).first();

		if (document == null) {
			UserProfile userProfile = new UserProfile(id);
			collection.insertOne(Document.parse(userProfile.toJson()));
			return userProfile;
		} else {
			JsonWriterSettings jsonWriterSettings = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
			return UserProfile.fromJson(document.toJson(jsonWriterSettings));
		}
	}


}
