package presenter.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class PhotoSlide extends Slide {

	public PhotoSlide(File path) {
		setImage(path.getAbsolutePath());
	}

	private void setImage(String path) {
		setContent(new Image(Display.getCurrent(), path));
	}

	public Class<Image> getContentType() {
		return Image.class;
	}
}
