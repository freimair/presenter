package presenter.model;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

import presenter.model.content.Content;

public class Slide {

	protected Content slide;
	protected Content notes;
	private Time checkpoint;

	public Slide(Element slideNode, File base) {
		load(slideNode, base);
	}

	public Slide(Content slide) {
		this.slide = slide;
	}

	public Displayable getSlide() {
		return slide;
	}

	/**
	 * @return the {@link Content} if existing, the {@link Slide} if not
	 */
	public Displayable getNotes() {
		if (null == notes)
			return slide;
		return notes;
	}

	public void setNotes(Content notes) {
		this.notes = notes;
	}

	public void setCheckpoint(Time time) {
		checkpoint = time;
	}

	public Time getCheckpoint() {
		return checkpoint;
	}

	public Element save(File base) throws IOException {
		Element result = new Element("slide");

		if (null != checkpoint)
			result.setAttribute("checkpoint", checkpoint.toString());

		Element contentNode = new Element("content");
		contentNode.setAttribute("type", slide.getClass().getSimpleName());
		slide.save(contentNode, base);
		result.addContent(contentNode);

		if (null != notes) {
			Element notesNode = new Element("notes");
			notesNode.setAttribute("type", notes.getClass().getSimpleName());
			notes.save(notesNode, base);
			result.addContent(notesNode);
		}

		return result;
	}

	public void load(Element slideNode, File base) {
		try {
			checkpoint = new Time(slideNode.getAttributeValue("checkpoint"));
		} catch (NullPointerException e) {
			// no checkpoint set. does not bother us.
		}

		slide = Content.create(slideNode.getChild("content"), base);

		if (null != slideNode.getChild("notes"))
			notes = Content.create(slideNode.getChild("notes"), base);
	}

}
