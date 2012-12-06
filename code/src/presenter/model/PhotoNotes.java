package presenter.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.jdom.Element;

import presenter.FileUtils;
import presenter.ImageUtils;

public class PhotoNotes extends Notes {

	private File photoFile;

	public PhotoNotes() {
		// essential for instantiating notes dynamically
	}

	public PhotoNotes(File file) {
		setImage(file);
	}

	private void setImage(File file) {
		photoFile = file;
		setContent(ImageUtils.load(photoFile));
	}

	@Override
	public Class<? extends Object> getContentType() {
		return Image.class;
	}

	@Override
	public void save(Element notesNode, File base) throws IOException {
		notesNode.addContent(FileUtils.getRelativePath(base, photoFile));
	}

	@Override
	protected void load(Element notesNode, File base) {
		setImage(FileUtils.recreateAbsolutPath(base, notesNode.getText()));
	}
}
