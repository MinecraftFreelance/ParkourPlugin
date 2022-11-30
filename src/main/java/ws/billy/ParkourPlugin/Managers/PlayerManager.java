package ws.billy.ParkourPlugin.Managers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ws.billy.ParkourPlugin.Configuration.Messages;
import ws.billy.ParkourPlugin.ParkourPlugin;

import java.util.*;

public class PlayerManager {

	private static final Map<UUID, PlayerManager> _parkourPlayers = new HashMap<>();
	private final OfflinePlayer _player;
	private long _bestAttempt;
	private long _currentAttemptStart;
	private Location _checkpoint;

	private PlayerManager(final OfflinePlayer player, final long bestAttempt) {
		this._player = player;
		this._bestAttempt = bestAttempt;
	}

	public static PlayerManager getInstance(final OfflinePlayer player, final boolean fetchFromDatabase) {
		if (getParkourPlayers().containsKey(player.getUniqueId())) {
			return getParkourPlayers().get(player.getUniqueId());
		} else {
			if (fetchFromDatabase) {
				final PlayerManager manager = ParkourPlugin.getExecutor().get(player.getUniqueId());
				if (manager == null) {
					return new PlayerManager(player, -1);
				}
				return manager;
			}
			final PlayerManager om = new PlayerManager(player, -1);
			_parkourPlayers.put(player.getUniqueId(), om);
			return om;
		}
	}

	public static Map<UUID, PlayerManager> getParkourPlayers() {
		return _parkourPlayers;
	}

	public static void removePlayerReference(final PlayerManager manager) {
		_parkourPlayers.remove(manager);
	}

	public OfflinePlayer getPlayer() {
		return _player;
	}

	public long getBestAttempt() {
		return _bestAttempt;
	}

	public void setBestAttempt(final long bestAttempt) {
		this._bestAttempt = bestAttempt;
	}

	public void sendMessage(final String message) {
		if (!getPlayer().isOnline()) {
			return;
		}
		getPlayer().getPlayer().sendMessage(Messages.getPrefix() + message);
	}

	public Location getCheckpoint() {
		return _checkpoint;
	}

	public void setCheckpoint(final Location checkpoint) {
		this._checkpoint = checkpoint;
	}

	public long getCurrentAttemptStart() {
		return _currentAttemptStart;
	}

	public void setCurrentAttemptStart(final long currentAttemptStart) {
		this._currentAttemptStart = currentAttemptStart;
	}

}
