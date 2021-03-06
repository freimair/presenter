package presenter.model.content;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import presenter.FileUtils;
import presenter.ImageUtils;
import presenter.Settings;
import presenter.ui.SWTUtils;

public class PdfContent extends Content {

	private File path = null;
	private Integer pageNumber = 1;

	public PdfContent() {
		// essential for instantiating slides dynamically
	}

	public PdfContent(File path, int page) {
		this.path = path;
		this.pageNumber = page;
	}

	@Override
	public Class<? extends Object> getContentType() {
		return Image.class;
	}

	@Override
	public Object getContent() {
		if (null == content) {
			try {
			if (Settings.getGoWithJPedal()) {
					PdfDecoder decoder = new PdfDecoder();
					decoder.openPdfFile(path.getAbsolutePath());
					decoder.setPageParameters(10, pageNumber);
					decoder.useHiResScreenDisplay(true);
					content = new Image(Display.getCurrent(),
							SWTUtils.convertToSWT(decoder
									.getPageAsImage(pageNumber)));
			} else {

					File tmpfile = new File(path.getAbsoluteFile().getParent()
							+ File.separator + "deleteme.jpg");
					Process p = Runtime.getRuntime().exec(
							new String[] {
									Settings.getImageMagicBinaryLocation(),
									"-density",
									"300",
									"-quality",
									"90",
									path.getAbsolutePath() + "["
											+ (pageNumber - 1) + "]",
									tmpfile.getAbsolutePath() });
					p.waitFor();

					content = ImageUtils.load(tmpfile);

					tmpfile.delete();

			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PdfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

	@Override
	public void save(Element contentNode, File base) throws IOException {
		contentNode.addContent(FileUtils.getRelativePath(base, path));
		contentNode.setAttribute("page", pageNumber.toString());
	}

	@Override
	public void load(Element contentNode, File base) {
		path = FileUtils.recreateAbsolutPath(base, contentNode.getText());
		pageNumber = Integer.valueOf(contentNode.getAttributeValue("page"));
	}
}
