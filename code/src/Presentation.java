import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Presentation {

	// ############ STATICS ############
	private static Presentation instance = null;

	private static Presentation getInstance() {
		if (null == instance)
			instance = new Presentation();
		return instance;
	}

	public static void load(List<File> result) {
		if (1 < result.size())
			getInstance().loadPhotos(result);
		else if (result.get(0).getName().toLowerCase().endsWith(".pdf"))
			getInstance().loadFromPdf(result.get(0));
		else if (result.get(0).getName().toLowerCase().endsWith(".xml"))
			getInstance().loadFromXml(result.get(0));

	}

	public static Slide next() {
		return get(++getInstance().index);
	}

	public static Slide getCurrent() {
		return get(getInstance().index);
	}

	public static Slide previous() {
		return get(--getInstance().index);
	}

	public static Slide get(int index) {
		return getInstance().slides.get(index);
	}

	public static List<Slide> getSlides() {
		return getInstance().slides;
	}

	// ########## NON-STATICS ##########
	private List<Slide> slides;
	private int index;

	public Presentation() {
		slides = new ArrayList<Slide>();
	}

	private void loadPhotos(List<File> result) {
		for (File current : result)
			slides.add(new Slide(current));
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
