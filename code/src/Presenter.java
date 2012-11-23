import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class Presenter {
	public static void main(String[] args) {
		// PresenterControl myControl = new PresenterControl();
		// myControl.open();

		Display display = new Display();
		FileDialog fileDialog = new FileDialog(new Shell(display), SWT.MULTI
				| SWT.OPEN);
		fileDialog
				.setFilterExtensions(new String[] { "*.pdf", "*.xml", "*.jpg" });
		fileDialog.open();

		List<File> result = new ArrayList<File>();
		for (String current : fileDialog.getFileNames())
			result.add(new File(fileDialog.getFilterPath()
					+ System.getProperty("file.separator") + current));

		Presentation.load(result);

		EditDialog editDialog = new EditDialog(new Shell());
		editDialog.setBlockOnOpen(true);
		editDialog.open();
	}
}