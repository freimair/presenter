package presenter.model;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

public abstract class Notes extends Displayable {

	// ############ STATICS ############
	public static Notes create(Element current, File base) {
		try {
			Notes result = (Notes) Class.forName(
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

	public Element save(File base) throws IOException {
		Element result = new Element("notes");
		result.setAttribute("type", this.getClass().getSimpleName());

		save(result, base);

		return result;
	}

	protected abstract void save(Element result, File base) throws IOException;

	protected abstract void load(Element notesNode, File base);

}
