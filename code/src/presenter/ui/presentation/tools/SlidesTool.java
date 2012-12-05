package presenter.ui.presentation.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
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
import presenter.ui.presentation.PresenterControl;
import presenter.ui.presentation.ScaledImage;

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

		contentComposite.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				update();
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
		// - add dummy
		Canvas dummy = new Canvas(contentComposite, SWT.NONE);
		dummy.setLayoutData(new RowData(contentComposite.getParent()
				.getBounds().width / 2 - 95, SWT.DEFAULT));

		// - add slides
		Canvas current = null;
		for (final Slide currentSlide : Presentation.getSlides()) {
			int style;
			if (currentSlide.equals(Presentation.getCurrent()))
				style = SWT.NONE;
			else
				style = SWT.TRANSPARENT;
			final Canvas container = new Canvas(contentComposite, style);
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
				container.setBackground(new Color(getDisplay(), 150, 255, 150));
				current = container;
			} else
				container.setLayoutData(new RowData(150, 150));

			container.redraw();
		}

		// - add dummy
		dummy = new Canvas(contentComposite, SWT.NONE);
		dummy.setLayoutData(new RowData(contentComposite.getParent()
				.getBounds().width / 2 - 95, SWT.DEFAULT));

		contentComposite.layout();

		((ScrolledComposite) contentComposite.getParent()).setOrigin(
				current.getLocation().x
						- contentComposite.getParent().getBounds().width / 2
						+ 95, 0);
		contentComposite.getParent().layout();
	}

}
