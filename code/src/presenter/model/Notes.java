package presenter.model;

import org.jdom.Element;

public abstract class Notes extends Displayable {

	public Element save() {
		Element result = new Element("notes");
		result.setAttribute("type", this.getClass().getSimpleName());

		save(result);

		return result;
	}

	protected abstract void save(Element result);

}
