package presenter.model;

import java.io.File;


public abstract class Slide extends Displayable {

	private Notes notes;

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

}
