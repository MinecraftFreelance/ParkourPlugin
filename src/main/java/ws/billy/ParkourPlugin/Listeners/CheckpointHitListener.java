package ws.billy.ParkourPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import ws.billy.ParkourPlugin.CustomEvents.CheckpointReachedEvent;
import ws.billy.ParkourPlugin.Managers.ParkourManager;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.Listener;

public class CheckpointHitListener extends Listener {

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent e) {
		if (e.getTo() == null || e.getFrom().distance(e.getTo()) == 0D) {
			return;
		}
		final ParkourManager parkourManager = ParkourPlugin.getParkourManager();
		final Location location = new Location(e.getPlayer().getWorld(),
				Math.floor(e.getPlayer().getLocation().getX()),
				Math.floor(e.getPlayer().getLocation().getY()),
				Math.floor(e.getPlayer().getLocation().getZ()));
		final PlayerManager player = PlayerManager.getInstance(e.getPlayer(), false);

		// check if the block they're moving onto is a checkpoint
		if (parkourManager.isLocationCheckpoint(location)) {
			// check if the checkpoint is 1 more than the current checkpoint of player
			if (player.getCheckpoint() == null) {
				if (parkourManager.getCheckpointNumber(location) == 0) {
					player.setCheckpoint(parkourManager.getStart());
					final CheckpointReachedEvent startedEvent = new CheckpointReachedEvent(parkourManager, player, parkourManager.getStart());
					Bukkit.getPluginManager().callEvent(startedEvent);
					return;
				}
				return;
			}

			if (parkourManager.getCheckpointNumber(location) == parkourManager.getCheckpointNumber(player.getCheckpoint()) + 1) {
				player.setCheckpoint(location);
				final CheckpointReachedEvent startedEvent = new CheckpointReachedEvent(parkourManager, player, location);
				Bukkit.getPluginManager().callEvent(startedEvent);
			}
		}

	}

}
