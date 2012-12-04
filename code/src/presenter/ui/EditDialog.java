package presenter.ui;

import java.io.File;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import presenter.model.Presentation;
import presenter.model.Slide;


public class EditDialog extends ApplicationWindow {

	private Composite slidesComposite;

	public EditDialog(Shell parentShell) {
		super(null);

		addToolBar(SWT.FLAT);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setSize(1000, 700);
		slidesComposite = (Composite) super.createContents(parent);
		slidesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		update();

		return slidesComposite;
	}

	@Override
	protected Control createToolBarControl(Composite parent) {
		ToolBar toolbar = (ToolBar) super.createToolBarControl(parent);

		ToolItem addButton = new ToolItem(toolbar, SWT.PUSH);
		addButton.setText("add");
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI
						| SWT.OPEN);
				// fileDialog.setFilterExtensions(new String[] { "*.jpg" });
				fileDialog.open();

				for (String current : fileDialog.getFileNames()) {
					File currentFile = new File(fileDialog.getFilterPath()
							+ System.getProperty("file.separator")
							+ current);
					if (currentFile.getName().endsWith(".pdf")) {
						MessageBox dialog = new MessageBox(getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("extract notes from pdf?");
						dialog.setMessage("Do you want to use every second page of the pdf as notes for the previous one?");
						Presentation.getEditor().loadFromPdf(currentFile,
								SWT.YES == dialog.open());
					} else {
						Presentation.getEditor().add(currentFile);
					}
				}

				update();
			}
		});

		ToolItem exitButton = new ToolItem(toolbar, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				close();
			}
		});
		return toolbar;
	}

	public void update() {
		for (Control current : slidesComposite.getChildren())
			current.dispose();

		for (Slide current : Presentation.getSlides())
			new SlideItem(slidesComposite, SWT.None, current, this);

		slidesComposite.layout();
	}

}
