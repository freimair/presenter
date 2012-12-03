import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;


public class SlideItem extends Composite {

	private Slide mySlide;
	private EditDialog myDialog;

	public SlideItem(Composite parent, int style, Slide slide,
			EditDialog editDialog) {
		super(parent, style | SWT.BORDER);
		mySlide = slide;
		myDialog = editDialog;

		this.setLayout(new RowLayout(SWT.HORIZONTAL));

		final Canvas slideCanvas = new Canvas(this, SWT.BORDER);
		slideCanvas.setSize(100, 100);
		slideCanvas.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Image image = mySlide.getContent();
				gc.drawImage(image, 0, 0, image.getBounds().width,
						image.getBounds().height, 0, 0,
						slideCanvas.getBounds().width,
						slideCanvas.getBounds().height);
				gc.dispose();
			}
		});

		Composite notesComposite = new Composite(this, SWT.NONE);
		notesComposite.setLayout(new RowLayout(SWT.VERTICAL));

		Button addTextButton = new Button(notesComposite, SWT.PUSH);
		addTextButton.setEnabled(false);
		addTextButton.setText("add Text notes");

		Button addImageButton = new Button(notesComposite, SWT.PUSH);
		addImageButton.setText("add notes file");
		addImageButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.jpg" });
				Presentation.getEditor()
						.add(new File(fileDialog.getFileName()));
				myDialog.update();
			}
		});
	}
}
