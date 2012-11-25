import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Presentation {

	// ############ STATICS ############
	private static Presentation instance = null;

	private static void getInstance() {
		if (null == instance)
			instance = new Presentation();
	}

	public static void open(String path) {
		getInstance();
	}

	public static void create() {
		getInstance();
	}

	public static void add(List<File> files) {
		if (1 < files.size())
			instance.loadPhotos(files);
		else if (files.get(0).getName().toLowerCase().endsWith(".pdf"))
			instance.loadFromPdf(files.get(0));
		else if (files.get(0).getName().toLowerCase().endsWith(".xml"))
			instance.loadFromXml(files.get(0));
	}

	public static Slide next() {
		return get(++instance.index);
	}

	public static Slide getCurrent() {
		return get(instance.index);
	}

	public static Slide previous() {
		return get(--instance.index);
	}

	public static Slide get(int index) {
		return instance.slides.get(index);
	}

	public static List<Slide> getSlides() {
		return instance.slides;
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
