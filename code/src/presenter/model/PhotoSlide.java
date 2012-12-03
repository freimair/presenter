package presenter.model;

import java.io.File;

public class PhotoSlide extends Slide {

	public PhotoSlide(File path) {
		setImage(path.getAbsolutePath());
	}
}
