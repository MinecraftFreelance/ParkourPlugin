package ws.billy.ParkourPlugin.CustomEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ws.billy.ParkourPlugin.Managers.PlayerManager;

public class LeaderboardModified extends Event {
	public static final HandlerList HANDLERS_LIST = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public PlayerManager getPlayer() {
		return _player;
	}

	private final PlayerManager _player;

	public LeaderboardModified(final PlayerManager playerManager) {
		this._player = playerManager;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
