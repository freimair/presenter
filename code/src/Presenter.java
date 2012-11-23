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

		Presentation.load(fileDialog.getFileNames());

		EditDialog editDialog = new EditDialog(new Shell());
		editDialog.setBlockOnOpen(true);
		editDialog.open();
	}
}