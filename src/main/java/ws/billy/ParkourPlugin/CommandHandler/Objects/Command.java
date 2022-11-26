package ws.billy.ParkourPlugin.CommandHandler.Objects;

import ws.billy.ParkourPlugin.CommandHandler.Utility.CooldownManager;
import ws.billy.ParkourPlugin.Configuration.Messages;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_8_R3.command.ColouredConsoleSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Command implements CommandExecutor, Listener, TabExecutor {

	public static List<Command> getCommands () {
		return commands;
	}

	public static void addCommand(Command command) {
		commands.add(command);
	}

	private static final List<Command> commands = new ArrayList<>();

	public String getName () {
		return name;
	}

	public String getDescription () {
		return description;
	}

	public int getArguments () {
		return arguments;
	}

	public String getUsage () {
		return usage;
	}

	public String getPermission () {
		return permission;
	}

	private final String name; // name of command
	private final String description; // description of command
	private final int arguments; // minimum amount of arguments
	private final String usage; // usage
	private final String permission; // permission to use command
	private final CooldownManager cooldownManager = new CooldownManager();

	public Command (String name, String description, int arguments, String usage, String permission, int cooldown) {
		this.name = name;
		this.description = description;
		this.arguments = arguments;
		this.usage = usage;
		this.permission = permission;
		this.cooldown = cooldown;
		addCommand(this);
	}

	public int getCooldown () {
		return cooldown;
	}

	private final int cooldown; // amount of ticks for cooldown

	@Override
	public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
		List<String> returnlist = new ArrayList<>();
		returnlist.add("");
		return returnlist;
	}

	protected void commandReply (CommandSender target, String message, boolean includePrefix) {
		if(includePrefix) {target.sendMessage(Messages.getPrefix()+message);return;}
		target.sendMessage(message);
	}

	protected Boolean checkPermission (Player player) {
		return player.hasPermission(permission);
	}

	protected boolean command (CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		return false;
	}

	@Override
	public boolean onCommand (CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

		// Permission check
		if(sender instanceof Player && !checkPermission(((Player) sender))){
			commandReply(sender, Messages.getPermissionsErrorMessage(), false);
			return true;
		}

		if(args.length < getArguments()){
			if(getUsage() == null) {
				return false;
			} else {
				commandReply(sender, "§cInvalid Usage! §eUsage: §7/"+command.getName()+" "+getUsage().toLowerCase(), true);
				return true;
			}
		}

		if(!(sender instanceof ColouredConsoleSender) && getCooldown() != 0){
			// Time check
			assert sender instanceof Player;
			long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldown(((Player) sender).getPlayer().getUniqueId());
			if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) < this.getCooldown()){
				long cooldownleft = this.getCooldown() - TimeUnit.MILLISECONDS.toSeconds(timeLeft);
				commandReply(sender, "§cPlease do not spam this command! §7(" + cooldownleft + "s remaining)", true);
				return true;
			}

			cooldownManager.setCooldown(((Player) sender).getPlayer().getUniqueId(), System.currentTimeMillis());

		}


		// Execute command
		try {
			if(!command(sender, command, label, args)){
				commandReply(sender, "§cOh no! This command isn't finished :(", true);
			}
		} catch(Exception e){
			sender.sendMessage("§c§lAn error occurred whilst running §e/" + command.getName());
			sender.sendMessage("§7If this issue continues, please contact an administrator!");
			e.printStackTrace();
		}

		return true;

	}

}
