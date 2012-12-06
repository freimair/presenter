package presenter.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class Presentation {

	// ############ STATICS ############
	private static Presentation instance = null;

	private static void getInstance() {
		if (null == instance)
			instance = new Presentation();
	}

	public static void open(String path) {
		instance = new Presentation(new File(path));
	}

	public static void save(File file) {
		Document document = new Document(new Element(
				Presentation.class.getSimpleName()));
		Element root = document.getRootElement();
		if (null != instance.duration)
			root.setAttribute("duration", instance.duration.toString());
		for (Slide currentSlide : instance.slides)
			try {
				root.addContent(currentSlide.save(file.getParentFile()));
			} catch (Exception e) {
				e.printStackTrace();
			}

		try {
			file.delete();
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(root, new FileWriter(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void create() {
		getInstance();
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

	public static void moveTo(int index) {
		index = (index % instance.slides.size() + instance.slides.size())
				% instance.slides.size();
		instance.index = index;
	}

	public static void moveTo(Slide slide) {
		instance.index = instance.slides.indexOf(slide);
	}

	public static Slide get(int index) {
		index = (index % instance.slides.size() + instance.slides.size())
				% instance.slides.size();
		return instance.slides.get(index);
	}

	public static List<Slide> getSlides() {
		return instance.slides;
	}

	public static void setDuration(Time time) {
		instance.duration = time;
	}

	public static Time getDuration() {
		return instance.duration;
	}

	public static Time getNextCheckpoint() {
		for (int i = instance.index; i < instance.slides.size(); i++) {
			Time checkpoint = instance.slides.get(i).getCheckpoint();
			if (null != checkpoint)
				return checkpoint;
		}
		return null;
	}

	public static PresentationEditor getEditor() {
		return new PresentationEditor(instance.slides);
	}

	// ########## NON-STATICS ##########
	private List<Slide> slides;
	private int index = 0;
	private Time duration;

	/**
	 * for creating an empty presentation from scratch.
	 */
	public Presentation() {
		slides = new ArrayList<Slide>();
	}

	/**
	 * For loading a given presentation from file.
	 * 
	 * @param file
	 */
	public Presentation(File file) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Element root = builder.build(file).getRootElement();
			try {
				duration = new Time(root.getAttributeValue("duration"));
			} catch (NullPointerException e) {
				// no duration set. does not bother us
			}
			slides = new ArrayList<Slide>();
			for (Object current : root.getChildren("slide"))
				slides.add(new Slide((Element) current, file.getParentFile()));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
