package ws.billy.ParkourPlugin.Utility;

import java.util.concurrent.TimeUnit;

public class UtilTime {

	private final long _seconds;
	private final long _minutes;
	private final long _hours;
	private final long _day;

	public UtilTime(final long ms) {
		_day = TimeUnit.MILLISECONDS.toDays(ms);
		_hours = TimeUnit.MILLISECONDS.toHours(ms) - (_day * 24);
		_minutes = TimeUnit.MILLISECONDS.toMinutes(ms) - (TimeUnit.MILLISECONDS.toHours(ms) * 60);
		_seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - (TimeUnit.MILLISECONDS.toMinutes(ms) * 60);
	}

	public String getLongTimeFormatted() {
		final StringBuilder result = new StringBuilder();
		if (_day > 0) {
			result.append(_day).append(" days").append(" ");
		}
		if (_hours > 0) {
			result.append(_hours).append(" hours").append(" ");
		}
		if (_minutes > 0) {
			result.append(_minutes).append(" minutes").append(" ");
		}
		if (_seconds > 0) {
			result.append(_seconds).append(" seconds").append(" ");
		}
		final String diff = result.toString();
		return diff.isEmpty() ? "None" : diff;
	}

	public String getShortTimeFormatted() {
		final StringBuilder result = new StringBuilder();
		if (_day > 0) {
			result.append(_day).append("d").append(" ");
		}
		if (_hours > 0) {
			result.append(_hours).append("h").append(" ");
		}
		if (_minutes > 0) {
			result.append(_minutes).append("m").append(" ");
		}
		if (_seconds > 0) {
			result.append(_seconds).append("s").append(" ");
		}
		final String diff = result.toString();
		return diff.isEmpty() ? "None" : diff;
	}
}
