package presenter.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import presenter.ui.SWTUtils;

public class PdfNotes extends Notes {

	private File path;
	private int pageNumber;

	public PdfNotes(File file, int page) {
		path = file;
		pageNumber = page;
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

				return new Image(Display.getCurrent(),
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
