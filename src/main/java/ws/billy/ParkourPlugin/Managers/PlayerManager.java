package ws.billy.ParkourPlugin.Managers;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerManager extends Listener {

	@EventHandler
	private void playerLogout(final PlayerQuitEvent e) {
		final Optional<PlayerManager> playerManager = getParkourPlayers().stream().filter(pl -> pl.getPlayer().equals(e.getPlayer())).findFirst();
		playerManager.ifPresent(manager -> ParkourPlugin.getExecutor().updatePlayer(manager));
	}

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

	public void setBestAttempt(final long bestAttempt) {
		this._bestAttempt = bestAttempt;
	}

	private long _bestAttempt;

}
