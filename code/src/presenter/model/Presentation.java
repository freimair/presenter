package presenter.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import presenter.Presenter;


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

	public static void save(File file) {
		Document document = new Document(new Element(
				Presenter.class.getSimpleName()));
		Element root = document.getRootElement();
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

	public static Slide get(int index) {
		index = (index % instance.slides.size() + instance.slides.size())
				% instance.slides.size();
		return instance.slides.get(index);
	}

	public static List<Slide> getSlides() {
		return instance.slides;
	}

	public static PresentationEditor getEditor() {
		return new PresentationEditor(instance.slides);
	}

	// ########## NON-STATICS ##########
	private List<Slide> slides;
	private int index = 0;

	public Presentation() {
		slides = new ArrayList<Slide>();
	}
}
