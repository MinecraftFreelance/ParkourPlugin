package ws.billy.ParkourPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import ws.billy.ParkourPlugin.CustomEvents.CheckpointReachedEvent;
import ws.billy.ParkourPlugin.CustomEvents.LeaderboardModified;
import ws.billy.ParkourPlugin.Managers.PlayerManager;
import ws.billy.ParkourPlugin.ParkourPlugin;
import ws.billy.ParkourPlugin.Utility.Listener;
import ws.billy.ParkourPlugin.Utility.UtilTime;

public class CheckpointReachListener extends Listener {

	// only handles start event (sets start time)
	@EventHandler
	private void startCheckpointHit(final CheckpointReachedEvent e) {
		if (!e.isStartCheckpoint()) {
			return;
		}
		e.getPlayer().setCurrentAttemptStart(System.currentTimeMillis() / 1000);
		e.getPlayer().sendMessage("You have started the parkour! Good luck!");
	}

	// only handles mid checkpoints
	@EventHandler
	private void normalCheckpoint(final CheckpointReachedEvent e) {
		if (e.isStartCheckpoint() || e.isEndCheckpoint()) {
			return;
		}
		e.getPlayer().sendMessage("You have hit checkpoint #" + e.getParkour().getCheckpointNumber(e.getCheckpoint()) + "!");
	}

	// only handle end checkpoint
	@EventHandler
	private void endCheckpoint(final CheckpointReachedEvent e) {
		if (!e.isEndCheckpoint()) {
			return;
		}
		final long completionTime = System.currentTimeMillis() / 1000 - e.getPlayer().getCurrentAttemptStart();
		final String completionTimeString = new UtilTime(completionTime * 1000).getLongTimeFormatted();

		if (e.getPlayer().getBestAttempt() == -1) {
			e.getPlayer().sendMessage("You have set your parkour time to " + completionTimeString);
			e.getPlayer().setBestAttempt(completionTime);
			e.getPlayer().setCheckpoint(null);
			checkIfLeaderboard(e.getPlayer());
			return;
		}

		// new best time
		if (e.getPlayer().getBestAttempt() > completionTime) {
			e.getPlayer().sendMessage("You have broken your record! You finished in " + completionTimeString);
			e.getPlayer().setBestAttempt(completionTime);
			checkIfLeaderboard(e.getPlayer());
		} else {
			e.getPlayer().sendMessage("Parkour completed in " + completionTimeString);
			e.getPlayer().sendMessage("That wasn't enough to beat your record!");
			e.getPlayer().sendMessage("Your record is " + new UtilTime(e.getPlayer().getBestAttempt() * 1000).getLongTimeFormatted());
		}

		e.getPlayer().setCheckpoint(null);

	}

	private void checkIfLeaderboard(final PlayerManager playerManager) {
		if (ParkourPlugin.getLeaderboardManager().checkPotentialRecordScore(playerManager) != -1) {
			playerManager.sendMessage("You have set a new leaderboard record!");
			ParkourPlugin.getExecutor().updatePlayer(playerManager);
			final LeaderboardModified leaderboardModified = new LeaderboardModified(playerManager);
			Bukkit.getPluginManager().callEvent(leaderboardModified);
		}
	}

}
