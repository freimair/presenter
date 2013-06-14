package presenter.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import presenter.Settings;

public class SettingsDialog extends Dialog {

	private Text imagemagickText;
	private Button gowithjpedalCheckBox;

	public SettingsDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));
		GridData secondColumGridData = new GridData(200, SWT.DEFAULT);

		Label gowithjpedalLabel = new Label(container, SWT.NONE);
		gowithjpedalLabel.setText("use JPedal instead of imagemagick");
		gowithjpedalCheckBox = new Button(container, SWT.CHECK);
		gowithjpedalCheckBox.setLayoutData(secondColumGridData);
		gowithjpedalCheckBox.setSelection(Settings.getGoWithJPedal());
		new Label(container, SWT.NONE);

		Label imagemagickLabel = new Label(container, SWT.NONE);
		imagemagickLabel.setText("imagemagick binary location");
		imagemagickText = new Text(container, SWT.BORDER);
		imagemagickText.setLayoutData(secondColumGridData);
		imagemagickText.setText(Settings.getImageMagicBinaryLocation());
		Button imagemagickButton = new Button(container, SWT.PUSH);
		imagemagickButton.setText("Browse...");
		imagemagickButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				imagemagickText.setText(dialog.open());
			}
		});

		return parent;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void okPressed() {
		Settings.setImageMagicBinaryLocation(imagemagickText.getText());
		Settings.setGoWithJPedal(gowithjpedalCheckBox.getSelection());
		super.okPressed();
	}
}
