package ws.billy.ParkourPlugin.Configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import ws.billy.ParkourPlugin.ParkourPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

	public void setConfigurationFile(final File configurationFile) {
		this._configurationFile = configurationFile;
	}

	public JSONObject defaultConfig() {
		return new JSONObject("{\n" + "  \"checkpointsData\": [\n" +
				"    {\n" + "      \"worldName\": \"world\",\n" +
				"      \"x\": 258,\n" + "      \"y\": 69,\n" +
				"      \"z\": 38\n" + "    },\n" + "    {\n" +
				"      \"worldName\": \"world\",\n" + "      \"x\": 245,\n" +
				"      \"y\": 74,\n" + "      \"z\": 30\n" + "    },\n" +
				"    {\n" + "      \"worldName\": \"world\",\n" +
				"      \"x\": 233,\n" + "      \"y\": 78,\n" +
				"      \"z\": 44\n" + "    },\n" + "    {\n" +
				"      \"worldName\": \"world\",\n" + "      \"x\": 233,\n" +
				"      \"y\": 79,\n" + "      \"z\": 64\n" + "    }\n" + "  ]\n" + "}");
	}

	public void generateIfNull() {
		if (_configurationFile.exists()) {
			return;
		}
		try {
			ParkourPlugin.getInstance().getDataFolder().mkdir();
			_configurationFile.createNewFile();
			final FileWriter file = new FileWriter(_configurationFile);
			file.write(defaultConfig().toString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			final Location checkpoint = new Location(Bukkit.getWorld(object.getString("worldName")), object.getDouble("x"), object.getDouble("y"), object.getDouble("z"));
			result.add(checkpoint);
			ParkourPlugin.log("Created new checkpoint for parkour at " + checkpoint);
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
			result.add(new JSONObject(object.toString()));
		}
		return result;
	}

}
