package ws.billy.ParkourPlugin;

import io.github.classgraph.ClassGraph;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ws.billy.ParkourPlugin.Database.Adapter.PlayerManagerAdapter;
import ws.billy.ParkourPlugin.Database.DatabaseExecutor;
import ws.billy.ParkourPlugin.Listeners.CheckpointHitListener;
import ws.billy.ParkourPlugin.Listeners.CheckpointReachListener;
import ws.billy.ParkourPlugin.Listeners.WorldguardListener;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.Scoreboard.Scoreboard;
import ws.billy.ParkourPlugin.Database.DatabaseConnection;
import ws.billy.ParkourPlugin.Managers.LeaderboardManager;
import ws.billy.ParkourPlugin.Managers.ParkourManager;
import ws.billy.ParkourPlugin.Configuration.Configuration;

import java.io.File;

public class ParkourPlugin extends JavaPlugin implements Listener {

	private static Configuration configuration;
    private static ParkourPlugin instance;
    private static PluginManager pluginManager;
	private static LeaderboardManager leaderboardManager;
	private static ParkourManager parkourManager;
	private static DatabaseConnection database;
	private static DatabaseExecutor executor;
	private static PlayerManagerAdapter adapter;
	private static Scoreboard scoreboard;

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

	@Override
	public void onEnable() {
		instance = this;
		pluginManager = Bukkit.getPluginManager();

		// event registers
		log("Loading plugin events");
		registerEventListeners();

		// get configuration information
		log("Loading plugin configuration");
		registerConfiguration();

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
		registerDatabase();

		// create plugin specific objects
		log("Loading leaderboards");
		registerLeaderboard();
	}

	private void registerEventListeners() {
		new CheckpointReachListener();
		new CheckpointHitListener();
		new WorldguardListener();
		getPluginManager().registerEvents(this, this);
	}

	private void registerConfiguration() {
		configuration = new Configuration();
		configuration.setConfigurationFile(new File(this.getDataFolder(), "checkpoints.json"));
		configuration.generateIfNull();
	}

	private void registerDatabase() {
		database = new DatabaseConnection(System.getenv("DATABASE_HOST"),
				Long.parseLong(System.getenv("DATABASE_PORT")),
				System.getenv("DATABASE_USERNAME"),
				System.getenv("DATABASE_PASSWORD"),
				System.getenv("DATABASE_DATABASE"));
		executor = new DatabaseExecutor(getDatabase());
		adapter = new PlayerManagerAdapter();
		getDatabase().getExecutor().registerAdapter(PlayerManager.class, adapter);
	}

	private void registerLeaderboard() {
		leaderboardManager = new LeaderboardManager(getExecutor().getTopFivePlayers().stream().toList());
		parkourManager = new ParkourManager(getConfigurationManager().getCheckpointLocations());
		scoreboard = new Scoreboard();
	}

	@EventHandler
	private void playerLogin(final PlayerJoinEvent e) {
		PlayerManager.getInstance(e.getPlayer(), true).setCheckpoint(null);
	}

	@EventHandler
	private void playerLogout(final PlayerQuitEvent e) {
		PlayerManager playerManager = PlayerManager.getInstance(e.getPlayer(), false);
		ParkourPlugin.getExecutor().updatePlayer(playerManager);
		// remove the player object if they're not on the leaderboard
		if (getLeaderboardManager().isOnLeaderboard(playerManager)) {
			return;
		}
		PlayerManager.removePlayerReference(playerManager);
	}

}
