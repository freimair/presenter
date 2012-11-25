import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


public class EditDialog extends ApplicationWindow {

	public EditDialog(Shell parentShell) {
		super(null);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = (Composite) super.createContents(parent);
		container.setLayout(new RowLayout(SWT.VERTICAL));

		for (Slide current : Presentation.getSlides())
			new SlideItem(container, SWT.None, current);

		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText("add");
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI
						| SWT.OPEN);
				fileDialog.setFilterExtensions(new String[] { "*.jpg" });
				fileDialog.open();

				List<File> result = new ArrayList<File>();
				for (String current : fileDialog.getFileNames())
					result.add(new File(fileDialog.getFilterPath()
							+ System.getProperty("file.separator") + current));

				Presentation.add(result);
			}
		});

		return container;
	}

}
