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

public class PdfNotes extends Notes {

	private File path;
	private Integer pageNumber;

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
	public void save(Element notesNode, File base) throws IOException {
		notesNode.addContent(FileUtils.getRelativePath(base, path));
		notesNode.setAttribute("page", pageNumber.toString());
	}
}
