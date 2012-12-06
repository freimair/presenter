package presenter.model;



public class Time implements Comparable<Time>, Cloneable {

	private int seconds = 0;

	public Time(int seconds) {
		this.seconds = seconds;
	}

	public Time(int hours, int minutes, int seconds) {

		this.seconds += hours * 60 * 60;
		this.seconds += minutes * 60;
		this.seconds += seconds;
	}

	public Time(String text) {
		String[] tmp = text.replace("-", "").split(":");

		for (int i = tmp.length - 1; i >= 0; i--)
			seconds += Integer.parseInt(tmp[i])
					* Math.pow(60, tmp.length - 1 - i);

		if (text.startsWith("-"))
			seconds = -seconds;
	}

	public void decrease() {
		seconds--;
	}

	public Time subtract(Time t) {
		return new Time(seconds - t.seconds);
	}

	public Time add(Time t) {
		return new Time(seconds + t.seconds);
	}

	@Override
	public int compareTo(Time o) {
		return o.seconds - seconds;
	}

	@Override
	public Time clone() {
		return new Time(seconds);
	}

	public String toString() {
		int hours = this.seconds / (60 * 60);
		int remainder = this.seconds % (60 * 60);

		int minutes = remainder / 60;
		remainder = remainder % 60;

		int seconds = remainder;

		return (0 > seconds ? "-" : "")
				+ ((Math.abs(hours) < 10 ? "0" : "") + Math.abs(hours) + ":"
						+ (Math.abs(minutes) < 10 ? "0" : "")
						+ Math.abs(minutes) + ":"
						+ (Math.abs(seconds) < 10 ? "0" : "") + Math
							.abs(seconds));
	}
}
