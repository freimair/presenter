package presenter.ui;
import java.io.File;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import presenter.model.PdfNotes;
import presenter.model.Slide;


public class SlideItem extends Composite {

	private Slide mySlide;
	private EditDialog myDialog;
	private Menu dropMenu;

	public SlideItem(Composite parent, int style, Slide slide,
			EditDialog editDialog) {
		super(parent, style | SWT.BORDER);
		mySlide = slide;
		myDialog = editDialog;

		this.setLayout(new GridLayout(2, false));

		ToolBar toolbar = new ToolBar(this, SWT.FLAT);
		GridData layout = new GridData();
		layout.horizontalSpan = 2;
		toolbar.setLayoutData(layout);

		ToolItem addTextButton = new ToolItem(toolbar, SWT.PUSH);
		addTextButton.setEnabled(false);
		addTextButton.setText("add Text notes");

		ToolItem addNotesButton = new ToolItem(toolbar, SWT.DROP_DOWN);
		addNotesButton.setText("add notes");
		addNotesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dropMenu == null) {
					dropMenu = new Menu(getShell(), SWT.POP_UP);
					getShell().setMenu(dropMenu);

					MenuItem addTextNotesItem = new MenuItem(dropMenu, SWT.PUSH);
					addTextNotesItem.setText("add text notes");
					addTextNotesItem
							.addSelectionListener(new SelectionAdapter() {
								@Override
								public void widgetSelected(SelectionEvent e) {
								}

							});
					MenuItem addNotesFileItem = new MenuItem(dropMenu, SWT.PUSH);
					addNotesFileItem.setText("add notes file");
					addNotesFileItem
							.addSelectionListener(new SelectionAdapter() {

								@Override
								public void widgetSelected(SelectionEvent e) {
									FileDialog fileDialog = new FileDialog(
											getShell());
									fileDialog.open();
									File notesFile = new File(fileDialog
											.getFileName());

									if (notesFile.getName().endsWith(".pdf")) {
										InputDialog dialog = new InputDialog(
												getShell(),
												"",
												"please select the pdf page number",
												"1", null);
										dialog.open();
										// dialog.getValue()
										mySlide.addNotes(new PdfNotes(
												notesFile, Integer
														.valueOf(dialog
																.getValue())));
										
									} else
										mySlide.addNotesFromFile(notesFile);

									myDialog.update();
								}
							});
				}

				final ToolItem toolItem = (ToolItem) e.widget;
				final ToolBar toolBar = toolItem.getParent();
				Rectangle rect = toolItem.getBounds();
				Point point = toolBar.toDisplay(new Point(rect.x, rect.y
						+ rect.height));
				dropMenu.setLocation(point.x, point.y);
				dropMenu.setVisible(true);
			}
		});

		final Canvas slideCanvas = new Canvas(this, SWT.BORDER);
		slideCanvas.setSize(100, 100);
		slideCanvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Image image = (Image) mySlide.getContent();
				gc.drawImage(image, 0, 0, image.getBounds().width,
						image.getBounds().height, 0, 0,
						slideCanvas.getBounds().width,
						slideCanvas.getBounds().height);
				gc.dispose();
			}
		});

		Canvas notesCanvas = new Canvas(this, SWT.BORDER);
		notesCanvas.setSize(100, 100);
		notesCanvas.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Image image;
				try {
					image = (Image) mySlide.getNotes().getContent();
				} catch (NullPointerException ex) {
					image = (Image) mySlide.getContent();
				}
				gc.drawImage(image, 0, 0, image.getBounds().width,
						image.getBounds().height, 0, 0,
						slideCanvas.getBounds().width,
						slideCanvas.getBounds().height);
				gc.dispose();
			}
		});

	}
}
