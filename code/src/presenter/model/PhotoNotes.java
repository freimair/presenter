package presenter.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;

import presenter.FileUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

public class PhotoNotes extends Notes {

	private File photoFile;

	public PhotoNotes() {
		// essential for instantiating notes dynamically
	}

	public PhotoNotes(File file) {
		photoFile = file;
		setImage();
	}

	private void setImage() {
		Image input = new Image(Display.getCurrent(),
				photoFile.getAbsolutePath());

		// go check if we have to rotate it
		Image result = null;
		try {
			Metadata metaData = ImageMetadataReader.readMetadata(photoFile);
			if (metaData.containsDirectory(ExifIFD0Directory.class)) {
				int angle = 0;
				switch (metaData.getDirectory(ExifIFD0Directory.class).getInt(
						ExifIFD0Directory.TAG_ORIENTATION)) {
				case 6:
					angle = 90;
					break;
				case 8:
					angle = -90;
					break;
				}

				if (0 != angle) {
					result = new Image(Display.getCurrent(),
							input.getBounds().height, input.getBounds().width);
					GC gc = new GC(result);
					gc.setAdvanced(true);

					Rectangle b = input.getBounds();

					Transform transform = new Transform(Display.getCurrent());
					// The rotation point is the center of the image
					transform.translate(b.height / 2, b.width / 2);
					// Rotate
					transform.rotate(angle);
					// Back to the orginal coordinate system
					transform.translate(-b.width / 2, -b.height / 2);
					gc.setTransform(transform);
					gc.drawImage(input, 0, 0);
					gc.dispose();
				}
			}
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null == result)
				result = input;
		}

		setContent(result);
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
		photoFile = FileUtils.recreateAbsolutPath(base, notesNode.getText());
		setImage();
	}
}
