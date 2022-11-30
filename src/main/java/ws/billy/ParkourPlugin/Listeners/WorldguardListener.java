package ws.billy.ParkourPlugin.Listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.Listener;
import ws.billy.ParkourPlugin.Scoreboard.Scoreboard;

public class WorldguardListener extends Listener {

	// entry to region - creates playerManager
	@EventHandler
	public void onPlayerEnterRegion(final PlayerMoveEvent e) {
		if (e.getTo() == null || e.getFrom().distance(e.getTo()) == 0D) {
			return;
		}
		final Player p = e.getPlayer();
		final Location previousLoc = BukkitAdapter.adapt(e.getTo());
		final Location newLoc = BukkitAdapter.adapt(e.getFrom());
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet originalSet = query.getApplicableRegions(previousLoc);
		final ApplicableRegionSet newSet = query.getApplicableRegions(newLoc);
		for (final ProtectedRegion oldRegion : originalSet.getRegions()) {
			if (oldRegion.getId().equalsIgnoreCase("parkour_region")) {
				if (newSet.getRegions().contains(oldRegion)) {
					return;
				}
				Scoreboard.getInstance().show(p);
			}
		}
	}

	// exit from region
	@EventHandler
	public void onPlayerLeaveRegion(final PlayerMoveEvent e) {
		if (e.getTo() == null || e.getFrom().distance(e.getTo()) == 0D) {
			return;
		}
		final Player p = e.getPlayer();
		final Location previousLoc = BukkitAdapter.adapt(e.getFrom());
		final Location newLoc = BukkitAdapter.adapt(e.getTo());
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet originalSet = query.getApplicableRegions(previousLoc);
		final ApplicableRegionSet newSet = query.getApplicableRegions(newLoc);
		for (final ProtectedRegion oldRegion : originalSet.getRegions()) {
			if (oldRegion.getId().equalsIgnoreCase("parkour_region")) {
				if (newSet.getRegions().contains(oldRegion)) {
					return;
				}
				Scoreboard.getInstance().hide(p);
			}
		}
	}

}
