package ws.billy.ParkourPlugin.Database;

import org.bukkit.scheduler.BukkitRunnable;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public class DatabaseExecutor {
	private final DatabaseConnection _connection;
	private final String _table = ParkourPlugin.getPluginName().toLowerCase();

	public DatabaseExecutor(final DatabaseConnection connection) {
		_connection = connection;
		new BukkitRunnable() {
			@Override
			public void run() {
				autoSave();
			}
		}.runTaskTimer(ParkourPlugin.getInstance(), 300, 300);
		createTable();
	}

	public void createTable() {
		_connection.getExecutor().execute("CREATE TABLE IF NOT EXISTS " + _table + "(uuid VARCHAR(72) PRIMARY KEY, bestAttempt BIGINT);");
	}

	public void updatePlayer(final PlayerManager playerManager) {
		if (contains(playerManager.getPlayer().getUniqueId())) {
			update(playerManager);
		} else {
			insert(playerManager);
		}
	}

	private void insert(final PlayerManager playerManager) {
		_connection.getExecutor().execute("INSERT INTO " + _table + " VALUES(?, ?);", c -> {
			try {
				c.setString(1, playerManager.getPlayer().getUniqueId().toString());
				c.setLong(2, playerManager.getBestAttempt());
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		});
	}

	private void update(final PlayerManager playerManager) {
		_connection.getExecutor().execute("UPDATE " + _table + " SET bestAttempt = ? WHERE uuid = ?;", c -> {
			try {
				c.setLong(1, playerManager.getBestAttempt());
				c.setString(2, playerManager.getPlayer().getUniqueId().toString());
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public PlayerManager get(final UUID uuid) {
		return _connection.getExecutor().query("SELECT * FROM " + _table + " WHERE uuid = ?;", c -> {
			try {
				c.setString(1, uuid.toString());
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}, PlayerManager.class).orElse(null);
	}

	public boolean contains(final UUID uuid) {
		return get(uuid) != null;
	}

	public Set<PlayerManager> getAll() {
		return _connection.getExecutor().queryMany("SELECT * FROM " + _table + ";", PlayerManager.class);
	}

	public Set<PlayerManager> getTopFivePlayers() {
		return ParkourPlugin.getDatabase().getExecutor().queryMany("SELECT * FROM " + _table + " WHERE NOT bestAttempt = -1 ORDER BY bestAttempt ASC LIMIT 5;", PlayerManager.class);
	}

	public void autoSave() {
		ParkourPlugin.log("Automatically saving parkour statistics");
		PlayerManager.getParkourPlayers().forEach(this::updatePlayer);
		ParkourPlugin.log("Finished saving parkour statistics");
	}

}
