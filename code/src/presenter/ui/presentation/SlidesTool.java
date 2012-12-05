package presenter.ui.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
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
		this.setLayout(new FillLayout());
		ScrolledComposite scrolledContentComposite = new ScrolledComposite(
				this, SWT.H_SCROLL);
		scrolledContentComposite.setLayout(new FillLayout());
		contentComposite = new Composite(scrolledContentComposite, SWT.NONE);
		scrolledContentComposite.setContent(contentComposite);
		scrolledContentComposite.setExpandHorizontal(true);
		scrolledContentComposite.setExpandVertical(true);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.center = true;
		layout.wrap = false;
		contentComposite.setLayout(layout);

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

		Canvas current = null;
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
			
			container.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent e) {
					Presentation.moveTo(currentSlide);
					getControl().update();
				}
			});

			if (currentSlide.equals(Presentation.getCurrent())) {
				container.setLayoutData(new RowData(190, 190));
				current = container;
			} else
				container.setLayoutData(new RowData(150, 150));

			container.redraw();
		}

		contentComposite.layout();
		((ScrolledComposite) contentComposite.getParent()).showControl(current);
	}

}
