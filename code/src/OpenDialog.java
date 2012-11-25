import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class OpenDialog extends TitleAreaDialog implements Runnable {

	private Combo workspaceCombo;

	public OpenDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();

		setTitle("Select Presentation");
		setMessage(
				"Presenter allows you to open a saved presentation or to create a new one.",
				IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));

		// FIXME somehow there is a label existing right after instantiating a
		// composite. This label is visible as a thin gray line and breaks the
		// whole layout
		for (Control current : container.getChildren())
			current.dispose();

		Label label = new Label(container, SWT.NONE);
		label.setText("Presentation");

		workspaceCombo = new Combo(container, SWT.DROP_DOWN | SWT.BORDER);
		workspaceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button browseButton = new Button(container, SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { ".xml" });
				try {
					workspaceCombo.add(dialog.open(), 0);
					workspaceCombo.select(0);
				} catch (NullPointerException e) {
					// dialog got cancelled
				}
			}
		});

		update();

		return container;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		// add "create new" button

		createButton(parent, IDialogConstants.IGNORE_ID, "New", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.IGNORE_ID == buttonId) {
			newPressed();
			return;
		}
		super.buttonPressed(buttonId);
	}

	public void update() {
		workspaceCombo.removeAll();
		for (String current : Settings.getRecent())
			workspaceCombo.add(current);
		workspaceCombo.select(0);
	}

	@Override
	protected void okPressed() {
		String path = workspaceCombo.getText();
		Presentation.open(path);
		Settings.setRecent(path);

		super.okPressed();
	}

	protected void newPressed() {
		Presentation.create();
		setReturnCode(OK);
		close();
	}

	@Override
	public void run() {
		setBlockOnOpen(true);

		open();
	}
}
