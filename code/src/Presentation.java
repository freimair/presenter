import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class Presentation {

	// ############ STATICS ############
	private static Presentation instance = null;

	private static Presentation getInstance() {
		if (null == instance)
			instance = new Presentation();
		return instance;
	}

	public static void load(File path) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Element root = builder.build(path).getRootElement();
			for (Object current : root.getChildren())
				getInstance().slides.add(new Slide((Element) current));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		// TODO Auto-generated constructor stub
	}

}
