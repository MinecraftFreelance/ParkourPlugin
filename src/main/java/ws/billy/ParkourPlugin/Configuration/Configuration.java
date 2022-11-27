package ws.billy.ParkourPlugin.Configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

	public void setConfigurationFile(final File configurationFile) {
		this._configurationFile = configurationFile;
	}

	private File _configurationFile;

	private JSONArray getJsonFromFile() {
		try {
			final byte[] data;
			final FileInputStream inputStream;
			inputStream = new FileInputStream(_configurationFile);
			data = new byte[(int) _configurationFile.length()];
			inputStream.read(data);
			inputStream.close();
			final String str = new String(data, StandardCharsets.UTF_8);
			return new JSONObject(str).getJSONArray("checkpointsData");
		} catch (final Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private boolean checkConfigurationFileExists() {
		if (_configurationFile == null) {
			return false;
		}
		return _configurationFile.exists();
	}

	public List<Location> getCheckpointLocations() {
		final List<Location> result = new ArrayList<>();
		final List<JSONObject> checkpointsJSON = getCheckpointsJSON();
		if (checkpointsJSON == null) {
			return null;
		}
		for (final JSONObject object : checkpointsJSON) {
			result.add(new Location(Bukkit.getWorld(object.getString("worldName")), object.getDouble("x"), object.getDouble("y"), object.getDouble("z")));
		}
		return result;
	}

	private List<JSONObject> getCheckpointsJSON() {
		if (!checkConfigurationFileExists()) {
			return null;
		}
		final List<JSONObject> result = new ArrayList<>();
		final JSONArray array = getJsonFromFile();
		for (final Object object : array) {
			result.add(new JSONObject(object));
		}
		return result;
	}

}
