package presenter.ui.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import presenter.model.Presentation;

public class ProgressTool extends Tool {

	private ProgressBar progressBar;
	private Label progressLabel;

	public ProgressTool(Composite parent, int style, PresenterControl control) {
		super(parent, style, control);

		this.setLayout(new GridLayout(2, false));
		int totalSlides = Presentation.getSlides().size() - 1;

		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setMaximum(totalSlides);

		progressLabel = new Label(this, SWT.RIGHT);
		FontData newFontData = new FontData(progressLabel.getFont()
				.getFontData()[0].toString());
		newFontData.height = 25;
		progressLabel.setFont(new Font(Display.getCurrent(), newFontData));

		// set fixed width
		progressLabel.setText(totalSlides + "/" + totalSlides);
		this.layout();
		progressLabel.setLayoutData(new GridData(
				progressLabel.getBounds().width, SWT.DEFAULT));
	}

	public void update() {
		int totalSlides = Presentation.getSlides().size();
		int currentSlide = Presentation.getSlides().indexOf(
				Presentation.getCurrent()) + 1;

		progressBar.setSelection(currentSlide - 1);
		progressLabel.setText(currentSlide + "/" + totalSlides);
	}


}
