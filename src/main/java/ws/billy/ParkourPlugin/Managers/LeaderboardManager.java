package ws.billy.ParkourPlugin.Managers;

import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import ws.billy.ParkourPlugin.CustomEvents.LeaderboardModified;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.Listener;

import java.util.List;
import java.util.Optional;

public class LeaderboardManager extends Listener {

	public List<PlayerManager> getLeaderboard() {
		return _top5Players;
	}

	private List<PlayerManager> _top5Players;

	public LeaderboardManager(final List<PlayerManager> initialLeaderboard) {
		_top5Players = initialLeaderboard;
	}

	public Optional<PlayerManager> getLeaderboardPlayer(final int position) {
		return Optional.ofNullable(_top5Players.get(position));
	}

	/***
	 * Submit a potential record score, returns an int based on where the person is in the top 5 (or -1)
	 * @param playerManager the player you're checking has entered the top 5
	 * @return an integer representing the new position of the player (or -1 if not qualified)
	 */
	public int checkPotentialRecordScore(final PlayerManager playerManager) {
		final int position = 0;
		if (_top5Players.isEmpty()) {
			return 1;
		}
		for (final PlayerManager toBeat : _top5Players) {
			if (playerManager.getBestAttempt() > toBeat.getBestAttempt()) {
				return position;
			}
		}
		return -1;
	}

	public boolean isOnLeaderboard(final PlayerManager playerManager) {
		return _top5Players.contains(playerManager);
	}

	public void updateLeaderboard() {
		_top5Players = ParkourPlugin.getExecutor().getTopFivePlayers().stream().toList();
	}

	@EventHandler
	private void leaderboardUpdate(final LeaderboardModified e) {
		updateLeaderboard();
	}

}
