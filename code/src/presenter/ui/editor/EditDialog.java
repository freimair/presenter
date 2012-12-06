package presenter.ui.editor;

import java.io.File;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import presenter.Settings;
import presenter.model.Presentation;
import presenter.model.Slide;
import presenter.model.Time;
import presenter.ui.OpenDialog;
import presenter.ui.presentation.PresenterControl;


public class EditDialog extends ApplicationWindow {

	private Composite slidesComposite;
	private int size = 500;
	private Text durationText;

	public EditDialog(Shell parentShell) {
		super(null);

		addToolBar(SWT.FLAT);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setSize(1050, 700);

		Composite container = (Composite) super.createContents(parent);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(1, false));

		Composite durationComposite = new Composite(container, SWT.NONE);
		durationComposite.setLayout(new GridLayout(2, false));
		Label durationLabel = new Label(durationComposite, SWT.NONE);
		durationLabel.setText("planned duration of the talk: ");
		durationText = new Text(durationComposite, SWT.BORDER | SWT.TRAIL);
		try {
			durationText.setText(Presentation.getDuration().toString());
		} catch (NullPointerException e) {
			durationText.setText("00:00:00");
		}

		durationText.setToolTipText("the planned duration of the talk");
		durationText.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int caretPosition = durationText.getCaretPosition();
				if (0 == (caretPosition + 1) % 3)
					caretPosition++;

				durationText.setSelection(caretPosition, caretPosition + 1);
			}
		});
		durationText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if ('0' > e.character || '9' < e.character)
					e.doit = false;
				if (durationText.getText().length() <= e.start)
					e.doit = false;
				if (!e.doit)
					durationText.clearSelection();
			}
		});
		durationText.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (Pattern.matches("\\d\\d:\\d\\d:\\d\\d",
						durationText.getText()))
					Presentation.setDuration(new Time(durationText.getText()));
				else
					durationText.setText("00:00:00");
			}
		});

		ScrolledComposite scrolledSlideComposite = new ScrolledComposite(
				container, SWT.V_SCROLL);
		scrolledSlideComposite.setLayout(new FillLayout());
		scrolledSlideComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrolledSlideComposite.setExpandHorizontal(true);
		scrolledSlideComposite.setExpandVertical(true);
		slidesComposite = new Composite(scrolledSlideComposite, SWT.NONE);
		scrolledSlideComposite.setContent(slidesComposite);
		slidesComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		slidesComposite.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_GRAY));

		slidesComposite.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Rectangle r = slidesComposite.getParent().getClientArea();
				((ScrolledComposite) slidesComposite.getParent())
						.setMinSize(slidesComposite.computeSize(r.width,
								SWT.DEFAULT));
			}
		});

		update();

		return slidesComposite;
	}

	@Override
	protected Control createToolBarControl(Composite parent) {
		ToolBar toolbar = (ToolBar) super.createToolBarControl(parent);

		ToolItem addButton = new ToolItem(toolbar, SWT.PUSH);
		addButton.setText("add slide");
		addButton.setToolTipText("add new Slide from pdf or image");
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

		ToolItem zoomInButton = new ToolItem(toolbar, SWT.PUSH);
		zoomInButton.setText("zoom in");
		zoomInButton.setToolTipText("increase thumbnail size");
		zoomInButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				size *= 1.11111f;
				update();
			}
		});

		ToolItem zoomOutButton = new ToolItem(toolbar, SWT.PUSH);
		zoomOutButton.setText("zoom out");
		zoomOutButton.setToolTipText("decrease thumbnail size");
		zoomOutButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				size *= 0.9f;
				update();
			}
		});

		ToolItem startButton = new ToolItem(toolbar, SWT.PUSH);
		startButton.setText("Start presentation");
		startButton.setToolTipText("show the presentation windows");
		startButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Presentation.moveTo(0);
				PresenterControl dialog = new PresenterControl();
				dialog.open();
			}
		});

		ToolItem saveButton = new ToolItem(toolbar, SWT.PUSH);
		saveButton.setText("Save");
		saveButton.setToolTipText("save the presentation to disk");
		saveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] { "*.presentation" });
				String result = dialog.open();
				if (null == result)
					return; // cancelled

				if (!result.endsWith(".presentation"))
					result += ".presentation";

				Presentation.save(new File(result));
				Settings.setRecent(result);
			}
		});

		ToolItem openButton = new ToolItem(toolbar, SWT.PUSH);
		openButton.setText("Open");
		openButton.setToolTipText("load a presentation from disk");
		openButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Dialog dialog = new OpenDialog(getShell());
				dialog.open();
				update();
			}
		});

		ToolItem exitButton = new ToolItem(toolbar, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.setToolTipText("Close the application");
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

		try {
			durationText.setText(Presentation.getDuration().toString());
		} catch (NullPointerException e) {
			durationText.setText("00:00:00");
		}

		slidesComposite.redraw();
		slidesComposite.layout();
	}

	public int getTileWidth() {
		return size;
	}

}
