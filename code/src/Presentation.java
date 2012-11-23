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

	public static void load(String[] paths) {
		if (1 < paths.length)
			getInstance().loadPhotos(paths);
		else if (paths[0].toLowerCase().endsWith(".pdf"))
			getInstance().loadFromPdf(paths[0]);
		else if (paths[0].toLowerCase().endsWith(".xml"))
			getInstance().loadFromXml(paths[0]);

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

	private void loadPhotos(String[] path) {
		for (String current : path)
			slides.add(new Slide(new File(current)));
	}

	private void loadFromPdf(String path) {
		System.out.println("pdf");

	}

	private void loadFromXml(String path) {
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
