package presenter.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
		try {
			PdfDecoder decoder = new PdfDecoder();
			decoder.openPdfFile(path.getAbsolutePath());
			// decoder.setPageParameters(1, pageNumber);
			decoder.decodePage(pageNumber);

			BufferedImage image = new BufferedImage(1000, 1000,
					BufferedImage.TYPE_INT_BGR);
			Graphics g = image.getGraphics();
			decoder.print(g);

			return new Image(Display.getCurrent(), SWTUtils.convertToSWT(image));
		} catch (PdfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
