package ws.billy.ParkourPlugin.Configuration;

import ws.billy.ParkourPlugin.ParkourPlugin;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class Messages {

	public static String getPrefix() {
		return ParkourPlugin.getPluginName();
	}

	public static String getPermissionsErrorMessage() {
		return "You do not have permission for this command!";
	}

}
