package com.matheus.servermanager.database.database;

public class DatabaseConfig {

	public String getHost() {
		return host;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isReconnectEnabled() {
		return reconnectEnabled;
	}

	public boolean isUnicode() {
		return unicode;
	}

	private final String host;
	private final String database;
	private final String username;
	private final String password;
	private final boolean reconnectEnabled;
	private final boolean unicode;
	private final boolean useSSL;

	private DatabaseConfig(String host, String database, String username, String password, boolean reconnectEnabled,
																boolean unicode, boolean useSSL) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.reconnectEnabled = reconnectEnabled;
		this.unicode = unicode;
		this.useSSL = useSSL;
	}

	public static Builder builder(String host, String database, String username, String password) {
		return new Builder(host, database, username, password);
	}

	public String getUrl() {
		StringBuilder url = new StringBuilder();
		url.append(String.format("jdbc:mysql://%s/%s", this.host, this.database));
		url.append("?");

		if (this.reconnectEnabled) {
			url.append("autoReconnect=true&");
		}

		if (this.unicode) {
			url.append("useUnicode=yes&characterEncoding=UTF-8");
		}

		if (!this.useSSL) {
			url.append("&useSSL=false");
		}

		return url.toString();
	}

	public static class Builder {

		private String host;
		private String database;
		private String username;
		private String password;
		private boolean reconnectEnabled;
		private boolean unicode;
		private boolean useSSL;

		Builder(String host, String database, String username, String password) {
			this.host = host;
			this.database = database;
			this.username = username;
			this.password = password;
			this.reconnectEnabled = false;
			this.unicode = true;
		}

		public DatabaseConfig build() {
			return new DatabaseConfig(this.host, this.database, this.username, this.password, this.reconnectEnabled, this.unicode, this.useSSL);
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}

		public Builder database(String database) {
			this.database = database;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder ssl(boolean useSSL) {
			this.useSSL = useSSL;
			return this;
		}

		public Builder reconnect(boolean reconnectEnabled) {
			this.reconnectEnabled = reconnectEnabled;
			return this;
		}

		public Builder unicode(boolean unicode) {
			this.unicode = unicode;
			return this;
		}
	}
}
