package ws.billy.ParkourPlugin.Utility;

import org.bukkit.Bukkit;
import ws.billy.ParkourPlugin.ParkourPlugin;

/**
 * Custom Listener class that automatically registers events and can be expanded to provide further functionality if required. <br>
 * NOTE: As this is a class, not an interface, extend it instead of implementing it. <br>
 *
 */
public class Listener implements org.bukkit.event.Listener { // Could possibly delete this and migrate registering events to the instantiateEventHandlers() method in CookieHub.java
	public Listener() {
		Bukkit.getPluginManager().registerEvents(this, ParkourPlugin.getInstance()); // Registers all events of subclass
		ParkourPlugin.log("Registered all events contained within class \"" + this.getClass().getSimpleName() + "\".");
	}
}

