package ws.billy.ParkourPlugin.CustomEvents;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ws.billy.ParkourPlugin.Managers.ParkourManager;
import ws.billy.ParkourPlugin.Managers.PlayerManager;

public class CheckpointReachedEvent extends Event {
	public static final HandlerList HANDLERS_LIST = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public PlayerManager getPlayer() {
		return _player;
	}

	public boolean isStartCheckpoint() {
		return _parkour.getStart().equals(_checkpoint);
	}

	public boolean isEndCheckpoint() {
		return _parkour.getEnd().equals(_checkpoint);
	}

	private final PlayerManager _player;

	public Location getCheckpoint() {
		return _checkpoint;
	}

	private final Location _checkpoint;

	public ParkourManager getParkour() {
		return _parkour;
	}

	private final ParkourManager _parkour;

	public CheckpointReachedEvent(final ParkourManager parkourManager, final PlayerManager playerManager, final Location checkpoint) {
		this._parkour = parkourManager;
		this._player = playerManager;
		this._checkpoint = checkpoint;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}
}
