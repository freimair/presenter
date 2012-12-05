package presenter.model;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

public abstract class Slide extends Displayable {

	// ############ STATICS ############
	public static Slide create(Element current, File base) {
		try {
			Slide result = (Slide) Class.forName(
					"presenter.model."
							+ current.getAttribute("type").getValue())
					.newInstance();
			result.load(current, base);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ########## NON-STATICS ##########

	protected Notes notes;

	/**
	 * @return the {@link Notes} if existing, the {@link Slide} if not
	 */
	public Displayable getNotes() {
		if (null == notes)
			return this;
		return notes;
	}

	public void addNotes(Notes notes) {
		this.notes = notes;
	}

	public void addNotesFromFile(File notesFile) {

	}

	public Element save(File base) throws IOException {
		Element result = new Element("slide");
		result.setAttribute("type", this.getClass().getSimpleName());

		Element xmlContent = new Element("content");
		result.addContent(xmlContent);
		saveContent(xmlContent, base);

		if (null != notes)
			result.addContent(notes.save(base));

		return result;
	}

	protected abstract void saveContent(Element contentNode, File base)
			throws IOException;

	public void load(Element slideNode, File base) {

		loadContent(slideNode.getChild("content"), base);

		if (null != slideNode.getChild("notes"))
			notes = Notes.create(slideNode.getChild("notes"), base);
	}

	protected abstract void loadContent(Element contentNode, File base);
}
