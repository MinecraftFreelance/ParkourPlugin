package ws.billy.ParkourPlugin.Database.Adapter;

import com.jaoow.sql.executor.adapter.SQLResultAdapter;
import org.bukkit.Bukkit;
import ws.billy.ParkourPlugin.Managers.PlayerManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerManagerAdapter implements SQLResultAdapter<PlayerManager> {
	@Override
	public PlayerManager adaptResult(final ResultSet rs) {
		try {
			final UUID uuid = UUID.fromString(rs.getString("uuid"));
			final long bestTime = rs.getLong("bestAttempt");
			final PlayerManager playerManager = PlayerManager.getInstance(Bukkit.getOfflinePlayer(uuid), false);
			playerManager.setBestAttempt(bestTime);
			return playerManager;
		} catch (final SQLException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
