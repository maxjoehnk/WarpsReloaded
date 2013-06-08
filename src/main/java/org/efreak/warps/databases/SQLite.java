package org.efreak.warps.databases;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.efreak.warps.Database;
import org.efreak.warps.WarpsReloaded;

/**
 * 
 * The SQLite Implementation of the Database
 *
 */

public class SQLite extends Database {

	@Override
	protected void connect() throws ClassNotFoundException, SQLException {
		File database = new File(WarpsReloaded.getInstance().getDataFolder(), config.getString("Database.File"));
		Class.forName("org.sqlite.JDBC");
		if (!database.exists())
			try {
				database.getAbsoluteFile().createNewFile();
			} catch (IOException e) {
				if (config.getDebug()) e.printStackTrace();
				io.sendConsoleError("Couldn't create database File");
			}
		dbConn = DriverManager.getConnection("jdbc:sqlite:" + database);
	}

	@Override
	protected void config() {
		config.update("Database.File", "database.db");
	}
}
