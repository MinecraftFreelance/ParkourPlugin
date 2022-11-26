package ws.billy.ParkourPlugin;

import io.github.classgraph.ClassGraph;
import ws.billy.ParkourPlugin.CommandHandler.Objects.Command;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public class ParkourPlugin extends JavaPlugin {

	private static ParkourPlugin instance;
	private static PluginManager pluginManager;

	public static ParkourPlugin getInstance () {return instance;}
	public static PluginManager getPluginManager () {return pluginManager;}

	public static String getPluginName() {return getInstance().getName();}

	public static void log(String log) {Bukkit.getLogger().log(Level.ALL, "["+getPluginName()+"] "+log);}

	public static void error(String log) {Bukkit.getLogger().warning("["+getPluginName()+"] "+log);}

	@Override
	public void onLoad() {
		enableReflection(getPluginName().toLowerCase());
	}

	/**
	 * Lets a Plugin "enable" reflection, by scanning all classes inside it, enabling the use of the ClassGraph reflection API by Core and itself. <br>
	 * <b> It is recommended that all plugins call this in their onLoad() function. </b>
	 */
	public static void enableReflection(String packageSuffix){
		new ClassGraph().acceptPackages("ws.billy."+packageSuffix).enableClassInfo().scan();
	}

	/**
	 * Uses reflection to register commands.
	 * @param plugin Java Plugin to use for registering the commands.
	 * @param packageSuffix plugin src path after net.thecookiemc. (e.g. for core, 'cookiecore')
	 */
	public static void loadCommands(JavaPlugin plugin, String packageSuffix){
		List<Class<Command>> commands = new ClassGraph().acceptPackages("ws.billy."+packageSuffix).enableClassInfo().scan().getSubclasses(Command.class).loadClasses(Command.class);

		commands.forEach(command -> { // Try to register each command
			try {
				plugin.getCommand(command.getSimpleName().toLowerCase()).setExecutor(command.newInstance());
				log("Registered command /" + command.getSimpleName().toLowerCase() + " successfully!");
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});

	}

	@Override
	public void onEnable () {

		instance = this;
		pluginManager = Bukkit.getPluginManager();

		// command registers
		log("Loading plugin commands");

		loadCommands(this, getPluginName().toLowerCase());

	}

}
