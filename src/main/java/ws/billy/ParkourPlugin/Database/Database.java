package ws.billy.ParkourPlugin.Database;

import org.bukkit.scheduler.BukkitRunnable;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;

import java.util.List;

public class Database {

	private final String _host;
	private final long _port;
	private final String _username;
	private final String _password;

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

	public Database(final String host, final long port, final String username, final String password) {
		this._host = host;
		this._port = port;
		this._username = username;
		this._password = password;
		connect();
		new BukkitRunnable() {
			@Override
			public void run () {
				//
			}
		}.runTaskTimer(ParkourPlugin.getInstance(), 300, 300);
	}

	public void autoSave() {
		ParkourPlugin.log("Automatically saving parkour statistics");
		PlayerManager.getParkourPlayers().forEach(this::updatePlayer);
		ParkourPlugin.log("Finished saving parkour statistics");
	}

	private void connect() {
		// connect to database
		ParkourPlugin.log("Database connection was successful!");
	}

	private void createTable() {
		//
	}

	public void updatePlayer(final PlayerManager playerManager) {
		//
	}

	public List<PlayerManager> getTopFivePlayers() {
		return null;
	}

}
