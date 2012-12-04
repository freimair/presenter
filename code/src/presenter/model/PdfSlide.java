package presenter.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import presenter.ui.SWTUtils;

public class PdfSlide extends Slide {

	private File path = null;
	private int pageNumber = 1;

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

}
