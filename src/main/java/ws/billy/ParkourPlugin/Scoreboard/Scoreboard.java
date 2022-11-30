package ws.billy.ParkourPlugin.Scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.UtilTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Scoreboard {

	private static Scoreboard _instance;

	public Scoreboard() {
		_instance = this;
	}

	public static Scoreboard getInstance() {
		return _instance;
	}

	private final Map<UUID, FastBoard> _boards = new HashMap<>();

	public void show(final Player player) {
		final FastBoard board = new FastBoard(player);
		board.updateTitle(ChatColor.GOLD + "Parkour");
		updateBoard(board, PlayerManager.getInstance(player, false));
		this._boards.put(player.getUniqueId(), board);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (board.isDeleted()) {
					this.cancel();
					return;
				}
				updateBoard(board, PlayerManager.getInstance(player, true));
			}
		}.runTaskTimer(ParkourPlugin.getInstance(), 20, 20);
	}

	public void hide(final Player player) {
		final FastBoard board = this._boards.remove(player.getUniqueId());
		if (board != null) {
			board.delete();
		}
	}

	private void updateBoard(final FastBoard board, final PlayerManager pm) {
		final String time = new UtilTime(pm.getBestAttempt() * 1000).getShortTimeFormatted();
		board.updateLines(
				" ",
				"Best Attempt: " + time,
				" ",
				"Leaderboard:"
		);
		int index = 1;
		for (final PlayerManager playerManager : ParkourPlugin.getLeaderboardManager().getLeaderboard()) {
			board.updateLine(index + 3, " #" + index + " - " + playerManager.getPlayer().getName() + " - " + new UtilTime(playerManager.getBestAttempt() * 1000).getShortTimeFormatted());
			index++;
		}
	}

}
