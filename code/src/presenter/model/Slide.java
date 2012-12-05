package presenter.model;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

public abstract class Slide extends Displayable {

	protected Notes notes;

	// public Slide(Element element) {
	// setImage(element.getChildText("content"));
	// if (null != element.getChildText("notes"))
	// notes = new Notes(element.getChildText("notes"));
	// }

	public Notes getNotes() {
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
}
