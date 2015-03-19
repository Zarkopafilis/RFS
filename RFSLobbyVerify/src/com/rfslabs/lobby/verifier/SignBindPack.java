package com.rfslabs.lobby.verifier;

import org.bukkit.block.Sign;

public class SignBindPack {

	public SignBindPack(Sign s , MinecraftServer server) {
		this.server = server;
		this.s = s;	
	}
	
	public Sign s;
	public MinecraftServer server;
	
}
