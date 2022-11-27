package ws.billy.ParkourPlugin.Managers;

import org.bukkit.OfflinePlayer;
import ws.billy.ParkourPlugin.Utility.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager extends Listener {

	public PlayerManager(final OfflinePlayer player, final long bestAttempt, final boolean persistent) {
		this._player = player;
		this._bestAttempt = bestAttempt;
		if (persistent) {
			_parkourPlayers.add(this);
		}
	}

	public static List<PlayerManager> getParkourPlayers() {
		return _parkourPlayers;
	}

	private static final List<PlayerManager> _parkourPlayers = new ArrayList<>();

	public OfflinePlayer getPlayer() {
		return _player;
	}

	public long getBestAttempt() {
		return _bestAttempt;
	}

	private final OfflinePlayer _player;
	private final long _bestAttempt;

}
