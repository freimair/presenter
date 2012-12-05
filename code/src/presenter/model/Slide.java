package presenter.model;

import java.io.File;

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

	public Element save() {
		Element result = new Element("slide");
		result.setAttribute("type", this.getClass().getSimpleName());

		Element xmlContent = new Element("content");
		result.addContent(xmlContent);
		saveContent(xmlContent);

		if (null != notes)
			result.addContent(notes.save());

		return result;
	}

	protected abstract void saveContent(Element contentNode);
}
