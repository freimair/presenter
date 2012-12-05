package presenter;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	/**
	 * Computes the path for a file relative to a given base, or fails if the
	 * only shared directory is the root and the absolute form is better.
	 * 
	 * @param base
	 *            File that is the base for the result
	 * @param name
	 *            File to be "relativized"
	 * @return the relative name
	 * @throws IOException
	 *             if files have no common sub-directories, i.e. at best share
	 *             the root prefix "/" or "C:\"
	 */

	public static String getRelativePath(File base, File name)
			throws IOException {
		File parent = base.getParentFile();

		if (parent == null) {
			throw new IOException("No common directory");
		}

		String bpath = base.getCanonicalPath();
		String fpath = name.getCanonicalPath();

		if (fpath.startsWith(bpath)) {
			return fpath.substring(bpath.length() + 1);
		} else {
			return (".." + File.separator + getRelativePath(parent, name));
		}
	}

	public static File recreateAbsolutPath(File base, String path) {
		return new File(base.getAbsolutePath() + File.separatorChar + path);
	}

}
