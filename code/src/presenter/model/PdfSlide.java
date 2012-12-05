package presenter.model;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jdom.Element;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import presenter.FileUtils;
import presenter.ui.SWTUtils;

public class PdfSlide extends Slide {

	private File path = null;
	private Integer pageNumber = 1;

	public PdfSlide() {
		// essential for instantiating slides dynamically
	}

	public PdfSlide(File path, int page) {
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
				PdfDecoder decoder = new PdfDecoder();
				decoder.openPdfFile(path.getAbsolutePath());
				decoder.setPageParameters(10, pageNumber);
				decoder.useHiResScreenDisplay(true);

				content = new Image(Display.getCurrent(),
						SWTUtils.convertToSWT(decoder
								.getPageAsImage(pageNumber)));
			} catch (PdfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

	@Override
	public void saveContent(Element contentNode, File base) throws IOException {
		contentNode.addContent(FileUtils.getRelativePath(base, path));
		contentNode.setAttribute("page", pageNumber.toString());
	}

	@Override
	protected void loadContent(Element contentNode, File base) {
		path = FileUtils.recreateAbsolutPath(base, contentNode.getText());
		pageNumber = Integer.valueOf(contentNode.getAttributeValue("page"));
	}

}
