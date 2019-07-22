package com.craftsteamg.helix;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Helix {

	public static void main(String[] args) throws LoginException {
		JDA jda = new JDABuilder()
				.setToken(args[0])
				.build();
	}


}
