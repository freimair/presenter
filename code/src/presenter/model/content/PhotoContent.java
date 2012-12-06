package presenter.model.content;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.jdom.Element;

import presenter.FileUtils;
import presenter.ImageUtils;

public class PhotoContent extends Content {

	private File photoFile;

	public PhotoContent() {
		// essential for instantiating slides dynamically
	}

	public PhotoContent(File file) {
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
	public void save(Element contentNode, File base) throws IOException {
		contentNode.addContent(FileUtils.getRelativePath(base, photoFile));
	}

	@Override
	public void load(Element contentNode, File base) {
		setImage(FileUtils.recreateAbsolutPath(base, contentNode.getText()));
	}
}
