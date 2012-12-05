package presenter.model;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

public abstract class Notes extends Displayable {

	public Element save(File base) throws IOException {
		Element result = new Element("notes");
		result.setAttribute("type", this.getClass().getSimpleName());

		save(result, base);

		return result;
	}

	protected abstract void save(Element result, File base) throws IOException;

}
