package ws.billy.ParkourPlugin.Managers;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;

import java.util.*;

public class ParkourManager {

	public List<Location> getLocations() {
		return _locations;
	}

	public int getCheckpointNumber(final Location location) {
		return ArrayUtils.indexOf(getLocations().toArray(), location);
	}

	private final List<Location> _locations;

	public ParkourManager(final List<Location> locations) {
		this._locations = locations;
	}

	public Location getStart() {
		return getLocations().get(0);
	}

	public Location getEnd() {
		return getLocations().get(getLocations().size() - 1);
	}

	public boolean isLocationCheckpoint(final Location location) {
		return getLocations().contains(location);
	}

}
