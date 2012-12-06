package presenter.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.jdom.Element;

import presenter.FileUtils;
import presenter.ImageUtils;

public class PhotoSlide extends Slide {

	private File photoFile;

	public PhotoSlide() {
		// essential for instantiating slides dynamically
	}

	public PhotoSlide(File file) {
		setImage(file);
	}
	
	private void setImage(File file) {
		photoFile = file;
		setContent(ImageUtils.load(photoFile));
	}

	public Class<Image> getContentType() {
		return Image.class;
	}

	@Override
	public void saveContent(Element contentNode, File base) throws IOException {
		contentNode.addContent(FileUtils.getRelativePath(base, photoFile));
	}

	@Override
	protected void loadContent(Element contentNode, File base) {
		setImage(FileUtils.recreateAbsolutPath(base, contentNode.getText()));
	}
}
