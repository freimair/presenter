import java.io.File;
import java.util.List;

public class PresentationEditor {

	private List<Slide> presentation;

	public PresentationEditor(List<Slide> slides) {
		presentation = slides;
	}

	public void add(File file) {
		if (file.getName().toLowerCase().matches(".*\\.jpe?g"))
			loadPhoto(file);
		else if (file.getName().toLowerCase().endsWith(".pdf"))
			loadFromPdf(file);
		else if (file.getName().toLowerCase().endsWith(".xml"))
			loadFromXml(file);
	}

	public void add(List<File> files) {
		for(File current : files)
			add(current);
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

	private void loadPhoto(File file) {
		presentation.add(new Slide(file));
	}

	public void loadFromPdf(File file) {
		loadFromPdf(file, false);
	}

	public void loadFromPdf(File file, boolean useEverySecondPageAsNotes) {
		System.out.println("pdf" + useEverySecondPageAsNotes);
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
