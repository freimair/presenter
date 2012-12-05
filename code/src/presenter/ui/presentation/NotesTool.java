package presenter.ui.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import presenter.model.Presentation;

public class NotesTool extends Tool {

	private Canvas notesCanvas;

	public NotesTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);
		
		this.setLayout(new FillLayout());
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		notesCanvas = new Canvas(this, SWT.NONE);
		notesCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				ScaledImage notes = new ScaledImage((Image) Presentation
							.getCurrent().getNotes().getContent(), notesCanvas
							.getBounds().width, notesCanvas.getBounds().height);

				e.gc.setAdvanced(true);
				e.gc.setAntialias(SWT.ON);
				e.gc.setInterpolation(SWT.HIGH);
				e.gc.drawImage(notes.getImage(),
						notes.centerX(notesCanvas.getBounds().width),
						notes.centerY(notesCanvas.getBounds().height));
		      }
		    });
	}

	@Override
	public void update() {
		notesCanvas.redraw();
	}

}
