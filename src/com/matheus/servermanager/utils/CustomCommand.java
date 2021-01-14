package com.matheus.servermanager.utils;

public class CustomCommand {

	private String cmd;
	private String server_msg;

	public CustomCommand(String cmd, String server_msg) {
		this.cmd = cmd;
		this.server_msg = server_msg;
	}

	public String getServer_msg(String playerName) {
		return server_msg.replace("{player}", playerName);
	}

	public String getCmd(String playerName) {
		return cmd.replace("{player}", playerName);
	}

}
