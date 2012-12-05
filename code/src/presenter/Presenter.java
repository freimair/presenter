package presenter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import presenter.ui.OpenDialog;
import presenter.ui.editor.EditDialog;


public class Presenter {
	public static void main(String[] args) {
		// PresenterControl myControl = new PresenterControl();
		// myControl.open();

		Display display = new Display();

		OpenDialog openDialog = new OpenDialog(new Shell(display));
		openDialog.open();
		if (Dialog.CANCEL == openDialog.getReturnCode())
			return;

		EditDialog editDialog = new EditDialog(new Shell(display));
		editDialog.setBlockOnOpen(true);
		editDialog.open();
	}
}