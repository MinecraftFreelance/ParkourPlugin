package ws.billy.ParkourPlugin;

import io.github.classgraph.ClassGraph;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ws.billy.ParkourPlugin.Database.DatabaseExecutor;
import ws.billy.ParkourPlugin.Utility.Listener;
import ws.billy.ParkourPlugin.Database.DatabaseConnection;
import ws.billy.ParkourPlugin.Managers.LeaderboardManager;
import ws.billy.ParkourPlugin.Managers.ParkourManager;
import ws.billy.ParkourPlugin.Configuration.Configuration;

import java.io.File;
import java.util.List;

public class ParkourPlugin extends JavaPlugin {
	private static Configuration configuration;
    private static ParkourPlugin instance;
    private static PluginManager pluginManager;
	private static LeaderboardManager leaderboardManager;
	private static ParkourManager parkourManager;
	private static DatabaseConnection database;
	private static DatabaseExecutor executor;

	public static DatabaseExecutor getExecutor() {
		return executor;
	}

	public static LeaderboardManager getLeaderboardManager() {
		return leaderboardManager;
	}

	public static Configuration getConfigurationManager() {
		return configuration;
	}

	public static ParkourManager getParkourManager() {
		return parkourManager;
	}

	public static DatabaseConnection getDatabase() {
		return database;
	}

    public static ParkourPlugin getInstance() {
        return instance;
	}

    public static PluginManager getPluginManager() {
		return pluginManager;
	}

	public static String getPluginName() {
		return getInstance().getClass().getSimpleName();
	}

	public static void log(final String log) {
		Bukkit.getLogger().info("[" + getPluginName() + "] " + log);
	}

	public static void error(final String log) {
		Bukkit.getLogger().severe("[" + getPluginName() + "] " + log);
	}

	@Override
	public void onLoad() {
		enableReflection(this.getClass().getSimpleName());
	}

	/**
	 * Lets a Plugin "enable" reflection, by scanning all classes inside it, enabling the use of the ClassGraph reflection API by Core and itself. <br>
	 * <b> It is recommended that all plugins call this in their onLoad() function. </b>
	 */
	public static void enableReflection(final String packageSuffix) {
		new ClassGraph().acceptPackages("ws.billy." + packageSuffix).enableClassInfo().scan();
	}

	/**
	 * Finds all subclasses of Listener through reflection, and creates a new instance of them, which allows them to register themselves.
	 */
	private static void instantiateEventHandlers() {
		for (final Class<?> classExtendingListener : getSubclasses("ws.billy." + getPluginName().toLowerCase(), Listener.class)) {
			try {
				classExtendingListener.newInstance();
			} catch (final Exception ignored) { } // Only real reason for this to happen is when a class with events has a
			// non-default constructor such as Jumper. This case can be ignored, as it gets instantiated anyway.
		}
	}

	/**
	 * Uses ClassGraph reflection API to get a list of subclasses of a class, inside a specific package.
	 */
	private static List<? extends Class<?>> getSubclasses(final String packageName, final Class<?> classObject) {
		return new ClassGraph().acceptPackages(packageName)
				.enableClassInfo().scan().getSubclasses(classObject)
				.loadClasses(classObject);
	}

	@Override
	public void onEnable() {
		instance = this;
		pluginManager = Bukkit.getPluginManager();

		// event registers
		log("Loading plugin events");
		instantiateEventHandlers();

		// get configuration information
		log("Loading plugin configuration");
		configuration = new Configuration();
		configuration.setConfigurationFile(new File(this.getDataFolder(), "checkpoints.json"));

		// setup and connect to SQL database
		log("Connecting to your SQL database");
		if (System.getenv("DATABASE_HOST") == null
				|| System.getenv("DATABASE_PORT") == null
				|| System.getenv("DATABASE_USERNAME") == null
				|| System.getenv("DATABASE_PASSWORD") == null
				|| System.getenv("DATABASE_DATABASE") == null) {
			error("You have not setup environment variables correctly!");
			getPluginManager().disablePlugin(this);
			return;
		}
		database = new DatabaseConnection(System.getenv("DATABASE_HOST"),
				Long.parseLong(System.getenv("DATABASE_PORT")),
				System.getenv("DATABASE_USERNAME"),
				System.getenv("DATABASE_PASSWORD"),
				System.getenv("DATABASE_DATABASE"));
		executor = new DatabaseExecutor(getDatabase());

		// create plugin specific objects
		log("Loading leaderboards");
		leaderboardManager = new LeaderboardManager(getExecutor().getTopFivePlayers().stream().toList());
		parkourManager = new ParkourManager(getConfigurationManager().getCheckpointLocations());

	}

}
