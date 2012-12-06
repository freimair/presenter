package presenter.model.content;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;

import presenter.model.Displayable;

public abstract class Content extends Displayable {

	// ############ STATICS ############
	public static Content create(Element current, File base) {
		try {
			Content result = (Content) Class.forName(
					Content.class.getName().replace(
							Content.class.getSimpleName(), "")
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
	public abstract void save(Element result, File base) throws IOException;

	public abstract void load(Element notesNode, File base);

}
