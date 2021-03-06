package presenter.ui.presentation.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import presenter.model.Presentation;
import presenter.ui.presentation.PresenterControl;
import presenter.ui.presentation.ScaledImage;

public class CurrentSlideTool extends Tool {

	private Canvas thumbnailCanvas;

	public CurrentSlideTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);
		thumbnailCanvas = new Canvas(this, SWT.NONE);
		thumbnailCanvas.setBounds(0, 0, 250, 250);
		thumbnailCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				ScaledImage thumbnail = new ScaledImage((Image) Presentation
						.getCurrent().getSlide().getContent(),
						thumbnailCanvas.getBounds().width, thumbnailCanvas
								.getBounds().height);

				e.gc.setAdvanced(true);
				e.gc.setAntialias(SWT.ON);
				e.gc.setInterpolation(SWT.HIGH);
				e.gc.drawImage(thumbnail.getImage(),
						thumbnail.centerX(thumbnailCanvas.getBounds().width),
						thumbnail.centerY(thumbnailCanvas.getBounds().height));
			}
		});
	}

	@Override
	public void update() {
		thumbnailCanvas.redraw();
	}

}
