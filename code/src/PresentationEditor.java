import java.io.File;
import java.util.List;

public class PresentationEditor {

	private List<Slide> presentation;

	public PresentationEditor(List<Slide> slides) {
		presentation = slides;
	}

	public void add(List<File> files) {
		if (1 < files.size())
			loadPhotos(files);
		else if (files.get(0).getName().toLowerCase().endsWith(".pdf"))
			loadFromPdf(files.get(0));
		else if (files.get(0).getName().toLowerCase().endsWith(".xml"))
			loadFromXml(files.get(0));
	}

	public void add(Slide slide) {

	}

	public void moveUp(Slide slide) {
		moveToPosition(slide, presentation.indexOf(slide) - 1);
	}

	public void moveDown(Slide slide) {
		moveToPosition(slide, presentation.indexOf(slide) + 1);
	}

	private void moveToPosition(Slide slide, int position) {
		presentation.remove(slide);
		presentation.add(position, slide);
	}

	private void loadPhotos(List<File> result) {
		for (File current : result)
			presentation.add(new Slide(current));
	}

	private void loadFromPdf(File file) {
		System.out.println("pdf");

	}

	private void loadFromXml(File file) {
		System.out.println("presentator file");
		// SAXBuilder builder = new SAXBuilder();
		// try {
		// Element root = builder.build(new File(path)).getRootElement();
		// for (Object current : root.getChildren())
		// slides.add(new Slide((Element) current));
		// } catch (JDOMException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
