package com.matheus.servermanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IPUtils {

	public static String getServerIP() {
		String ip = null;
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

}
