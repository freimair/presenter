package presenter.ui.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import presenter.model.Presentation;
import presenter.model.Slide;

public class SlidesTool extends Tool {

	private Composite contentComposite;

	public SlidesTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);
		this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.setLayout(new FillLayout());
		ScrolledComposite scrolledContentComposite = new ScrolledComposite(
				this, SWT.H_SCROLL);
		scrolledContentComposite.setLayout(new FillLayout());
		contentComposite = new Composite(scrolledContentComposite, SWT.NONE);
		scrolledContentComposite.setContent(contentComposite);
		scrolledContentComposite.setExpandHorizontal(true);
		scrolledContentComposite.setExpandVertical(true);
		contentComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		parent.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Rectangle r = contentComposite.getParent().getClientArea();
				((ScrolledComposite) contentComposite.getParent())
						.setMinSize(contentComposite.computeSize(SWT.DEFAULT,
								r.height));
				contentComposite.getParent().redraw();
			}
		});

		update();
	}

	@Override
	public void update() {
		// clear
		for (Control current : contentComposite.getChildren())
			current.dispose();

		// refill
		for (final Slide currentSlide : Presentation.getSlides()) {
			final Canvas container = new Canvas(contentComposite, SWT.BORDER);
			container.addPaintListener(new PaintListener() {

				public void paintControl(PaintEvent e) {
					ScaledImage thumbnail = new ScaledImage(
							(Image) currentSlide
							.getContent(), container.getBounds().width,
							container.getBounds().height);

					e.gc.setAdvanced(true);
					e.gc.setAntialias(SWT.ON);
					e.gc.setInterpolation(SWT.HIGH);
					e.gc.drawImage(
							thumbnail.getImage(),
							thumbnail.centerX(container.getBounds().width),
							thumbnail.centerY(container.getBounds().height));
				}
			});
			
			if (currentSlide.equals(Presentation.getCurrent())) {
				container.setLayoutData(new RowData(190, 190));
				// TODO make sure this item is placed in the exact center of the
				// composite
			} else
				container.setLayoutData(new RowData(150, 150));

			container.redraw();
		}
		contentComposite.layout();
	}

}
