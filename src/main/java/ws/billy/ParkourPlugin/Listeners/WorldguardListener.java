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
import ws.billy.ParkourPlugin.Utility.Listener;
import ws.billy.ParkourPlugin.Scoreboard.Scoreboard;

public class WorldguardListener extends Listener {

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent e) {
		if (e.getFrom().distance(e.getTo()) == 0D) {
			return;
		}
		final Player p = e.getPlayer();
		final Location loc = BukkitAdapter.adapt(p.getLocation());
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		final ApplicableRegionSet set = query.getApplicableRegions(loc);
		for (final ProtectedRegion region : set.getRegions()) {
			if (region.getId().equalsIgnoreCase("parkour_region")) {
				Scoreboard.getInstance().show(p);
			}
		}
	}

}
