package presenter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;


public class Settings {

	private static final Preferences preferences = Preferences
			.userNodeForPackage(Presenter.class);

	private static final String recent = "recent";
	private static final String recentCount = "recentCount";

	public static void setRecent(String path) {
		if (getRecent().contains(path)) {
			// exchange
			preferences.put(recent + (getRecent().indexOf(path) + 1),
					preferences.get(recent + "1", ""));
			preferences.put(recent + "1", path);
		} else {
			// add new
			for (int i = Integer.valueOf(preferences.get(recentCount, "5")); i > 1; i--)
				preferences.put(recent + i, preferences.get(recent + (i - 1), ""));

			preferences.put(recent + "1", path);
		}
	}

	public static List<String> getRecent() {
		List<String> result = new ArrayList<String>();
		for (int i = 1; i <= Integer.valueOf(preferences.get(recentCount, "5")); i++) {
			String tmp = preferences.get(recent + i, "");
			if (!"".equals(tmp))
				result.add(tmp);
		}
		return result;
	}

	private static final String imagemagicklocation = "imagemagickbinary";

	public static String getImageMagicBinaryLocation() {
		return preferences.get(imagemagicklocation, "convert");
	}

	public static void setImageMagicBinaryLocation(String text) {
		preferences.put(imagemagicklocation, text);
	}

	private static final String gowithjpetal = "gowithjpetal";

	public static boolean getGoWithJPedal() {
		return preferences.get(gowithjpetal, "true").equals("true") ? true
				: false;
	}

	public static void setGoWithJPedal(boolean goWithJPedal) {
		preferences.put(imagemagicklocation, goWithJPedal ? "true" : "false");
	}
}
