package ws.billy.ParkourPlugin.Database;

import com.jaoow.sql.connector.SQLConnector;
import com.jaoow.sql.connector.type.impl.MySQLDatabaseType;
import com.jaoow.sql.executor.SQLExecutor;
import ws.billy.ParkourPlugin.ParkourPlugin;

import java.sql.SQLException;

public class DatabaseConnection {

	private final String _host;
	private final long _port;
	private final String _username;
	private final String _password;

	public String getDatabase() {
		return _database;
	}

	private final String _database;
	private SQLExecutor _executor;

	public SQLExecutor getExecutor() {
		return _executor;
	}

	public String getHost() {
		return _host;
	}

	public long getPort() {
		return _port;
	}

	public String getUsername() {
		return _username;
	}

	public String getPassword() {
		return _password;
	}

	public DatabaseConnection(final String host, final long port, final String username, final String password, final String database) {
		this._host = host;
		this._port = port;
		this._username = username;
		this._password = password;
		this._database = database;

		final MySQLDatabaseType mysql = MySQLDatabaseType.builder().address(host).database(_database).username(_username).password(_password).build();

		try {
			this._executor = new SQLExecutor(connect(mysql));
			ParkourPlugin.log("Database connection was successful!");
		} catch (final SQLException e) {
			ParkourPlugin.error("Error whilst connecting to database, plugin disabled.");
			ParkourPlugin.getPluginManager().disablePlugin(ParkourPlugin.getInstance());
			e.printStackTrace();
		}
	}

	private SQLConnector connect(final MySQLDatabaseType sql) throws SQLException {
		// connect to database
		return sql.connect();
	}
}
