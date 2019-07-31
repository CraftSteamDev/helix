package com.craftsteamg.helix;

import com.craftsteamg.helix.config.BotConfig;
import com.craftsteamg.helix.internal.BotManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helix {

	public static void main(String[] args) throws LoginException, IOException {

		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.setLenient()
				.create();

		InputStreamReader reader = new InputStreamReader(Helix.class.getResourceAsStream("/config.json"));
		BotConfig config = gson.fromJson(reader, BotConfig.class);
		reader.close();

		JDA jda = new JDABuilder()
				.setToken(config.getBotToken())
				.build();

		MongoClient dbClient = createDBClient(config.getDbUsername(), config.getDbPassword(), config.getDbIP(), config.getDbName());
		MongoDatabase database = dbClient.getDatabase("pokepalooza");


		try {
			database.createCollection("messageLogs");
		}catch (MongoCommandException e) {
			System.out.println("Collection already made, skipping.");
		}


		BotManager manager = new BotManager(jda, database, config.getPrefix());
		manager.registerListeners();


		jda.addEventListener(new ListenerAdapter() {
			@Override
			public void onShutdown(@Nonnull ShutdownEvent event) {
				dbClient.close();
			}
		});

	}


	private static MongoClient createDBClient(String userName, String pwd, String ip, String dbName) {
		return MongoClients.create("mongodb://" + userName + ":" + pwd + "@" + ip + "/" + dbName);
	}


}
